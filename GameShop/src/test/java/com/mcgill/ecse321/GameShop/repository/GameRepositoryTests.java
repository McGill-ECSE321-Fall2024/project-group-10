package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Game pulledGame = gameRepository.findById(game.getGame_id());

        String email = "11@11.com";
        String username = "T";
        String password = "pass";
        String phoneNumber = "+1 (438) 210-1111";
        String address = "1100 rue Sainte-Catherine";
    
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);
    
        Category createdCategory = new Category("Actionssss", createdManager);
        createdCategory = categoryRepository.save(createdCategory);

        game.addCategory(createdCategory);
        game = gameRepository.save(game);


        Date startDate = Date.valueOf("2021-10-10");
        Date endDate = Date.valueOf("2021-10-20");

        Promotion createdPromotion = new Promotion("Promotion", 10, startDate, endDate, createdManager);
        createdPromotion.addGame(game);
        createdPromotion = promotionRepository.save(createdPromotion);
        Promotion pulledPromotion = promotionRepository.findById(createdPromotion.getPromotion_id());



        Platform createdPlatform = new Platform("PS5", createdManager);
        game.addPlatform(createdPlatform);
        createdPlatform = platformRepository.save(createdPlatform);
        game = gameRepository.save(game);

        String emailCustomer = "12@12.com";
        String usernameCustomer = "JudeSousou1";
        String passwordCustomer = "p12";
        String phoneNumberCustomer = "+1 (438) 320-239";
        String addressCustomer = "1110 rue University";

        Cart cart = new Cart();
        cart.addGame(game);
        cart = cartRepository.save(cart);
        Cart pulledCart = cartRepository.findById(cart.getCart_id());
     

        Customer customer1 = new Customer(emailCustomer, usernameCustomer, passwordCustomer, phoneNumberCustomer, addressCustomer, cart);
        customer1 = customerRepository.save(customer1);

        WishList wishlist = new WishList("title1", customer1);
        wishlist.addGame(game);
        wishlist = wishListRepository.save(wishlist);

        WishList wishListFromDb = wishListRepository.findById(wishlist.getWishList_id());



        //testing the game added to the wishlist
        assertNotNull(wishListFromDb);
        assertEquals(title, wishListFromDb.getGames().getFirst().getTitle(), "Title should match.");
        assertEquals(description, wishListFromDb.getGames().getFirst().getDescription(), "Description should match.");
        assertEquals(price, wishListFromDb.getGames().getFirst().getPrice());
        assertEquals(gameStatus, wishListFromDb.getGames().getFirst().getGameStatus());
        assertEquals(stockQuantity, wishListFromDb.getGames().getFirst().getStockQuantity());
        assertEquals(photoUrl, wishListFromDb.getGames().getFirst().getPhotoUrl());
        assertEquals("Actionssss", wishListFromDb.getGames().getFirst().getCategories().get(0).getCategoryName());
        assertEquals("PS5", wishListFromDb.getGames().getFirst().getPlatforms().get(0).getPlatformName());



        //getting the customer from the wishlist and checking that the game is properly added to the customer's cart
        //for customer we only need to check the email as that is the primary key
        assertEquals(emailCustomer, wishListFromDb.getCustomer().getEmail(), "Email should match.");
        assertEquals(title, wishListFromDb.getCustomer().getCart().getGames().getLast().getTitle(), "Title should match.");
        assertEquals(description, wishListFromDb.getCustomer().getCart().getGames().getLast().getDescription(), "Description should match.");
        assertEquals(1,pulledCart.getGames().size(),    "Cart should have 1 game.");
        assertEquals(title, pulledCart.getGames().getLast().getTitle(), "Game title should match.");
        assertEquals(cart.getCart_id(), pulledCart.getCart_id(), "Cart ID should match.");

        assertEquals(game.getGame_id(), pulledGame.getGame_id(), "Game ID should match.");
        assertEquals(title, pulledGame.getTitle(), "Title should match.");
        assertEquals(description, pulledGame.getDescription(), "Description should match.");
        assertEquals(price, pulledGame.getPrice(), "Price should match.");
        assertEquals(gameStatus, pulledGame.getGameStatus(),    "Game status should match.");
        assertEquals(stockQuantity, pulledGame.getStockQuantity(),  "Stock quantity should match.");
        assertEquals(photoUrl, pulledGame.getPhotoUrl(), "Photo URL should match.");
        assertEquals("Actionssss", pulledGame.getCategories().get(0).getCategoryName(), "Category name should match.");
        assertEquals("PS5", pulledGame.getPlatforms().get(0).getPlatformName(), "Platform name should match.");

        assertNotNull(pulledPromotion);
        assertEquals(1, pulledPromotion.getGames().size(),  "Promotion should have 1 game.");
        assertEquals(title, pulledPromotion.getGames().get(0).getTitle(), "Game title should match.");
        assertEquals(createdPromotion.getPromotion_id(), pulledPromotion.getPromotion_id(), "Promotion ID should match.");
        assertEquals(game.getGame_id(), pulledPromotion.getGames().get(0).getGame_id(), "Game ID should match.");

        

    }

    @Test
    @Transactional
    public void testCreateAndRetrieveGameWithMultipleCategoriesAndPlatforms() {
        String title = "tetris";
        String description = "Adventure Game";
        int price = 60;
        GameStatus gameStatus = GameStatus.InStock;
        int stockQuantity = 100;
        String photoUrl = "www.tetris.com/photo";

        // Create and save a Game
        Game game = new Game(title, description, price, gameStatus, stockQuantity, photoUrl);

        // Create Categories and Platforms
        Manager manager = new Manager("13@13.com", "Manager1", "pass123", "+1 (555) 555-5555", "1234 Manager St");
        manager = managerRepository.save(manager);

        Category actionCategory = new Category("Action22", manager);
        Category adventureCategory = new Category("Adventure22", manager);
        actionCategory = categoryRepository.save(actionCategory);
        adventureCategory = categoryRepository.save(adventureCategory);

        Platform ps5Platform = new Platform("PS5", manager);
        Platform switchPlatform = new Platform("Switch", manager);
        ps5Platform = platformRepository.save(ps5Platform);
        switchPlatform = platformRepository.save(switchPlatform);

        // Add Categories and Platforms to the Game
        game.addCategory(actionCategory);
        game.addCategory(adventureCategory);
        game.addPlatform(ps5Platform);
        game.addPlatform(switchPlatform);
        game = gameRepository.save(game);

        // Retrieve the game from the database
        Game pulledGame = gameRepository.findById(game.getGame_id());

        // Assertions
        assertNotNull(pulledGame);
        assertEquals(2, pulledGame.getCategories().size());
        assertEquals(2, pulledGame.getPlatforms().size());
        int firstCategoryId = actionCategory.getCategory_id();
        boolean foundFirstCategory = pulledGame.getCategories().stream().anyMatch(category -> category.getCategory_id() == firstCategoryId);
        assertTrue(foundFirstCategory, "First game should be in the cart.");
        int secondCategoryId = adventureCategory.getCategory_id();
        boolean foundSecondCategory = pulledGame.getCategories().stream().anyMatch(category -> category.getCategory_id() == secondCategoryId);
        assertTrue(foundSecondCategory, "Second game should be in the cart.");
        int firstPlatformId = ps5Platform.getPlatform_id();
        boolean foundFirstPlatform = pulledGame.getPlatforms().stream().anyMatch(platform -> platform.getPlatform_id() == firstPlatformId);
        assertTrue(foundFirstPlatform, "First platform should be in the cart.");
        int secondPlatformId = switchPlatform.getPlatform_id();
        boolean foundSecondPlatform = pulledGame.getPlatforms().stream().anyMatch(platform -> platform.getPlatform_id() == secondPlatformId);
        assertTrue(foundSecondPlatform, "Second platform should be in the cart."); 
}


    
}
