package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;

import jakarta.transaction.Transactional;

import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Promotion;
import com.mcgill.ecse321.GameShop.model.WishList;

@SpringBootTest
public class GameRepositoryTests {
    
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WishListRepository wishListRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        wishListRepository.deleteAll();
        promotionRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        platformRepository.deleteAll();
        managerRepository.deleteAll();
        
    }

    @Test
    @Transactional
    public void testCreateAndReadGame(){
        String title = "Super Mario";
        String description = "Mario";
        int price = 25;
        GameStatus gameStatus = GameStatus.InStock;
        int stockQuantity = 55;
        String photoUrl = "wwww.photo1.com";

        Game game = new Game(title, description, price, gameStatus, stockQuantity, photoUrl);
        game = gameRepository.save(game);

        Game gameFromDb = gameRepository.findById(game.getGame_id());

        String email = "Tarek@hotmail.co";
        String username = "T";
        String password = "pass";
        String phoneNumber = "+1 (438) 210-1111";
        String address = "1100 rue Sainte-Catherine";
    
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);
    
        Category createdCategory = new Category("Actionssss", createdManager);
        createdCategory = categoryRepository.save(createdCategory);
        int categoryId = createdCategory.getCategory_id();
        game.addCategory(createdCategory);
        game = gameRepository.save(game);


        Date startDate = Date.valueOf("2021-10-10");
        Date endDate = Date.valueOf("2021-10-20");

        Promotion createdPromotion = new Promotion("Promotion", 10, startDate, endDate, createdManager);
        createdPromotion.addGame(game);
        createdPromotion = promotionRepository.save(createdPromotion);


        Platform createdPlatform = new Platform("PS5", createdManager);
        game.addPlatform(createdPlatform);
        createdPlatform = platformRepository.save(createdPlatform);
        game = gameRepository.save(game);

        String emailCustomer = "judetest@gmail.com";
        String usernameCustomer = "JudeSousou1";
        String passwordCustomer = "p12";
        String phoneNumberCustomer = "+1 (438) 320-239";
        String addressCustomer = "1110 rue University";

        Cart cart = new Cart();
        cart.addGame(game);
        cart = cartRepository.save(cart);
        int cartId = cart.getCart_id();

        Customer customer1 = new Customer(emailCustomer, usernameCustomer, passwordCustomer, phoneNumberCustomer, addressCustomer, cart);
        customer1 = customerRepository.save(customer1);

        WishList wishlist = new WishList("title1", customer1);
        wishlist.addGame(game);
        wishlist = wishListRepository.save(wishlist);

        WishList wishListFromDb = wishListRepository.findById(wishlist.getWishList_id());

        //testing the game added to the wishlist
        assertNotNull(wishListFromDb);
        assertEquals(title, wishListFromDb.getGames().getFirst().getTitle());
        assertEquals(description, wishListFromDb.getGames().getFirst().getDescription());
        assertEquals(price, wishListFromDb.getGames().getFirst().getPrice());
        assertEquals(gameStatus, wishListFromDb.getGames().getFirst().getGameStatus());
        assertEquals(stockQuantity, wishListFromDb.getGames().getFirst().getStockQuantity());
        assertEquals(photoUrl, wishListFromDb.getGames().getFirst().getPhotoUrl());
        // assertEquals("Electronics", wishListFromDb.getGames().getFirst().getCategories().get(0).getCategoryName());

        //getting the customer from the wishlist and checking that the game is properly added to the customer's cart
        //for customer we only need to check the email as that is the primary key
        assertEquals(emailCustomer, wishListFromDb.getCustomer().getEmail());
        assertEquals(title, wishListFromDb.getCustomer().getCart().getGames().getLast().getTitle());
        assertEquals(description, wishListFromDb.getGames().getFirst().getDescription());
        assertEquals(price, wishListFromDb.getGames().getFirst().getPrice());
        assertEquals(gameStatus, wishListFromDb.getGames().getFirst().getGameStatus());
        assertEquals(stockQuantity, wishListFromDb.getGames().getFirst().getStockQuantity());
        assertEquals(photoUrl, wishListFromDb.getGames().getFirst().getPhotoUrl());

        assertEquals(title, gameFromDb.getTitle());
        assertEquals(description, gameFromDb.getDescription());
        assertEquals(price, gameFromDb.getPrice());
        assertEquals(gameStatus, gameFromDb.getGameStatus());
        assertEquals(stockQuantity, gameFromDb.getStockQuantity());
        assertEquals(photoUrl, gameFromDb.getPhotoUrl());










        


    }

    
}
