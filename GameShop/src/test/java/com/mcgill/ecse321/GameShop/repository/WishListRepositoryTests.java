package com.mcgill.ecse321.GameShop.repository;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.WishList;


@SpringBootTest
public class WishListRepositoryTests{
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        wishListRepository.deleteAll();
    }
    @Test
    public void testCreateAndReadWishList(){
        String email = "marc.germanos@hotmail.com";
        String username = "Marcg";
        String password = "password";
        int phoneNumber = 1234567890;
        String address = "120 rue Pen";
        String aTitle = "Marcg's wishlist";
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        

        Customer aCustomer = new Customer(email, username, password, phoneNumber, address, cart);
        aCustomer = customerRepository.save(aCustomer);
        WishList aWishList = new WishList(aTitle, aCustomer);

        Game game1 = new Game("Title", "Game 1 Description", 50, Game.GameStatus.InStock, 10, "http://photo1.url");
        
        Game game2 = new Game("Title", "Game 2 Description", 60, Game.GameStatus.InStock, 5, "http://photo2.url");
        game1 = gameRepository.save(game1);
        int game1Id = game1.getGame_id();
        game2 = gameRepository.save(game2);
        int game2Id = game2.getGame_id();

        // Create WishList and associate games
        aWishList.addGame(game1);
        aWishList.addGame(game2);
        
        aWishList = wishListRepository.save(aWishList);
        int wishList_id = aWishList.getWishList_id();

        WishList pulledWishList = wishListRepository.findById(wishList_id);
        
        assertNotNull(pulledWishList);
        assertEquals(aTitle, pulledWishList.getTitle());
        Customer pulledCustomer = pulledWishList.getCustomer();
        assertNotNull(pulledCustomer);
        assertEquals(email, pulledCustomer.getEmail());
        assertEquals(username, pulledCustomer.getUsername());
        assertEquals(password, pulledCustomer.getPassword());
        assertEquals(phoneNumber, pulledCustomer.getPhoneNumber());
        assertEquals(address, pulledCustomer.getAddress());

        List<Game> gameList = pulledWishList.getGames();
        assertNotNull(gameList);
        assertEquals(2, gameList.size());
        boolean wasFound = false;
        for (Game game : gameList){
            if (game.getGame_id() == game1Id){
                wasFound = true;
                break;
            }
        }
        assertTrue(wasFound);
        wasFound = false;
        for (Game game : gameList){
            if (game.getGame_id() == game2Id){
                wasFound = true;
                break;
            }
        }
        assertTrue(wasFound);/**/

    }
}