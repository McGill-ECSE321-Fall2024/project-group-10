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

import jakarta.transaction.Transactional;


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
        wishListRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndReadWishList(){
        String email = "marc.germanos@hotmail.com";
        String username = "Marcg";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9277";
        String address = "120 rue Pen";
        String aTitle = "Marc's wishlist";
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

    @Test
    @Transactional
    public void testMultipleWishListsPerCustomer() {
        // Create a Cart and Customer
        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Customer customer = new Customer("moh.abdelhady@moh.com", "mohamed", "password", "+1 (123) 123-4567", "132 Drummond", cart);
        customer = customerRepository.save(customer);

        // Create two WishLists for the same Customer
        WishList wishList1 = new WishList("Mohamed's First WishList", customer);
        WishList wishList2 = new WishList("Mohamed's Second WishList", customer);
        wishList1 = wishListRepository.save(wishList1);
        wishList2 = wishListRepository.save(wishList2);

        // Retrieve and verify the WishLists
        WishList pulledWishList1 = wishListRepository.findById(wishList1.getWishList_id());
        WishList pulledWishList2 = wishListRepository.findById(wishList2.getWishList_id());

        assertNotNull(pulledWishList1);
        assertNotNull(pulledWishList2);
        assertEquals(customer.getEmail(), pulledWishList1.getCustomer().getEmail(), "WishList1 should be associated with Mohamed.");
        assertEquals(customer.getEmail(), pulledWishList2.getCustomer().getEmail(), "WishList2 should be associated with Mohamed.");
}

    @Test
    @Transactional
    public void testUpdateWishListDetails() {
        // Create a Cart and Customer
        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Customer customer = new Customer("moh.abdelhady@moh.com", "mohamed", "password", "+1 (123) 123-4567", "132 Drummond", cart);
        customer = customerRepository.save(customer);

        // Create a WishList
        WishList wishList = new WishList("Mohamed's WishList", customer);
        wishList = wishListRepository.save(wishList);

        // Update WishList title
        wishList.setTitle("Mohamed's Updated WishList");
        wishList = wishListRepository.save(wishList);

        // Retrieve and verify the updated WishList
        WishList updatedWishList = wishListRepository.findById(wishList.getWishList_id());
        assertNotNull(updatedWishList);
        assertEquals("Mohamed's Updated WishList", updatedWishList.getTitle(), "WishList title should be updated.");
}

}