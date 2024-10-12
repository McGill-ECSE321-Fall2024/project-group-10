package com.mcgill.ecse321.GameShop.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.valueOf;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.SpecificGame;

import jakarta.transaction.Transactional;


@SpringBootTest
public class CartRepositoryTests {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private SpecificGameRepository specificgameRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional 
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
    // Delete orders first (if they reference accounts or carts)
    orderRepository.deleteAll();
    
    // Delete accounts next
    accountRepository.deleteAll();
    
    // Now it's safe to delete carts
    cartRepository.deleteAll();
    
    // Delete other entities
    specificgameRepository.deleteAll();
    gameRepository.deleteAll();
    
    }

    @Test
    @Transactional
    public void testCreateAndReadCart() {
        String aDescription = "Halo2";
        int aPrice = 50;
        GameStatus aGameStatus = GameStatus.InStock;
        int aStockQuantity = 10;
        String aPhotoUrl = "www.halo2.com";
        
        Game firstGame = new Game(aDescription, aPrice, aGameStatus, aStockQuantity, aPhotoUrl);
        firstGame = gameRepository.save(firstGame); // Save the game to ensure it's persisted
        SpecificGame firstSpecificGame = new SpecificGame(firstGame);
        firstSpecificGame = specificgameRepository.save(firstSpecificGame); // Save SpecificGame as well
        
        String aDescription2 = "CsGo";
        int aPrice2 = 15;
        GameStatus aGameStatus2 = GameStatus.OutOfStock;
        int aStockQuantity2 = 0;
        String aPhotoUrl2 = "www.csgo.com";
        
        Game secondGame = new Game(aDescription2, aPrice2, aGameStatus2, aStockQuantity2, aPhotoUrl2);
        secondGame = gameRepository.save(secondGame);
        SpecificGame secondSpecificGame = new SpecificGame(secondGame);
        secondSpecificGame = specificgameRepository.save(secondSpecificGame);
        
        Date aOrderDate = Date.valueOf("2022-04-05");
        
        // Step 1: Create and save Cart
        Cart cart = new Cart();
        cart.addGame(firstGame);  // Ensure the games are associated with the Cart
        cart.addGame(secondGame);
        cart = cartRepository.save(cart); // Persist the Cart first
        
        // Step 2: Create and save Customer with the Cart
        Customer aCustomer = new Customer("ajn@njr.cm", "ajn", "ajn2", 1234567890, "123 street", cart);
        aCustomer = accountRepository.save(aCustomer); // Ensure Customer is saved before the Order
        
        // Step 3: Create and save Order with the Customer
        Order order = new Order(aOrderDate, "notes of the order", 123456, aCustomer);
        order = orderRepository.save(order);  // Save the Order first
        orderRepository.flush(); // Force persistence
        assertNotNull(order.getTrackingNumber(), "Order's trackingNumber should not be null after saving.");
        
        // Step 4: Now associate the Order with the Cart and resave the Cart
        cart.setOrder(order);  // Set the order in the Cart
        cart = cartRepository.save(cart);  // Resave the Cart now that it has an Order
        
        // Step 5: Retrieve and validate Cart
        int cartId = cart.getCart_id();
        Cart pulledCart = cartRepository.findById(cartId);
        
        // Assert that pulledCart is not null
        assertNotNull(pulledCart);
        
        

        assertNotNull(pulledCart);
        pulledCart.getGames().size();
        assertNotNull(cart);
        assertEquals(2, pulledCart.getGames().size());
        assertNotNull(pulledCart.getOrder());
        assertEquals(order.getTrackingNumber(), pulledCart.getOrder().getTrackingNumber());
        assertEquals(Date.valueOf("2022-04-05"), pulledCart.getOrder().getOrderDate(), "Order date should be '2022-04-05'.");
        assertEquals("notes of the order", pulledCart.getOrder().getNote(), "Order notes should match.");
        assertTrue(pulledCart.getGames().contains(firstGame));
        assertTrue(pulledCart.getGames().contains(secondGame));


        assertEquals(GameStatus.InStock, pulledCart.getGames().get(0).getGameStatus(), "First game's status should be InStock.");
        assertEquals(GameStatus.OutOfStock, pulledCart.getGames().get(1).getGameStatus(), "Second game's status should be OutOfStock.");
        assertEquals("Halo2", pulledCart.getGames().get(0).getDescription(), "First game should be Halo2.");
        assertEquals("CsGo", pulledCart.getGames().get(1).getDescription(), "Second game should be CsGo.");
        assertEquals(50, pulledCart.getGames().get(0).getPrice(), "First game's price should be 50.");
        assertEquals(15, pulledCart.getGames().get(1).getPrice(), "Second game's price should be 15.");
   
        Customer retrievedCustomer = pulledCart.getOrder().getCustomer();
        assertNotNull(retrievedCustomer, "Customer should not be null.");
        assertEquals("ajn@njr.cm", retrievedCustomer.getEmail(), "Customer's email should be 'ajn@njr.cm'.");
        assertEquals(1234567890, retrievedCustomer.getPhoneNumber(), "Customer's phone number should match.");
        assertEquals("123 street", retrievedCustomer.getAddress(), "Customer's address should match.");
        assertEquals("ajn", retrievedCustomer.getUsername(), "Customer's username should match.");
        assertEquals("ajn2", retrievedCustomer.getPassword(), "Customer's password should match.");

    }



    }







