package com.mcgill.ecse321.GameShop.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.valueOf;

import java.sql.Date;
import java.util.List;

import javax.sound.sampled.AudioSystem;

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

import jakarta.transaction.Transactional;

import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.SpecificGame;


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
    private CustomerRepository customerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {

    // Fetch all customers and remove their cart references before deleting carts
    // Iterable<Account> customers = accountRepository.findAll();
    // for (Account customer : customers) {
    //     ((Customer)customer).deleteCart();
    //     System.out.println("ASKDBJASDLJASHDLJHASLJD: " + customer);
    //     accountRepository.save(customer); // Save the update to remove the cart reference
    // }

    // Now it is safe to delete carts
    specificgameRepository.deleteAll();
    orderRepository.deleteAll();
    customerRepository.deleteAll();
    cartRepository.deleteAll();
    gameRepository.deleteAll();

    }

    // Order --> Customer --> Cart --> GAME <-- SpecificGame

    @Test
    @Transactional
    public void testCreateAndReadCart() {
        System.out.println("ASKDBJASDLJASHDLJHASLJD: ");
        String aDescription = "Halo2";
        int aPrice = 50;
        GameStatus aGameStatus = GameStatus.InStock;
        int aStockQuantity = 10;
        String aPhotoUrl = "www.halo2.com";

        Game firstGame = new Game( aDescription+"A", aDescription,  aPrice,  aGameStatus,  aStockQuantity,  aPhotoUrl);
        firstGame = gameRepository.save(firstGame);

        SpecificGame firstSpecificGame = new SpecificGame(firstGame);
        firstSpecificGame = specificgameRepository.save(firstSpecificGame); 

        String aDescription2 = "CsGo";
        int aPrice2 = 15;
        GameStatus aGameStatus2 = GameStatus.OutOfStock;
        int aStockQuantity2 = 0;
        String aPhotoUrl2 = "www.csgo.com";
        
        Game secondGame = new Game( aDescription2 +"A", aDescription2,  aPrice2,  aGameStatus2,  aStockQuantity2,  aPhotoUrl2);
        secondGame = gameRepository.save(secondGame);

        SpecificGame secondSpecificGame = new SpecificGame(secondGame);
        secondSpecificGame = specificgameRepository.save(secondSpecificGame); 
        
        Date aOrderDate = Date.valueOf("2022-04-05");

        Cart cart = new Cart();
        cart.addGame(firstGame);
        cart.addGame(secondGame);
        cart = cartRepository.save(cart);

        Customer aCustomer = new Customer("ajn@njr.cm", "ajn", "ajn2", "1234567890", "123 street", cart);
        aCustomer = customerRepository.save(aCustomer); 

        Order order = new Order(aOrderDate,"notes of the order",123456,aCustomer);
        order = orderRepository.save(order);

        cart.setOrder(order);
        cart = cartRepository.save(cart);
        int cartId = cart.getCart_id();
        Cart pulledCart = cartRepository.findById(cartId);
        

        assertNotNull(pulledCart);
        pulledCart.getGames().size();
        assertNotNull(cart);
        assertEquals(2, pulledCart.getGames().size());
        assertNotNull(pulledCart.getOrder());
        assertEquals(order.getTrackingNumber(), pulledCart.getOrder().getTrackingNumber());
        assertEquals(Date.valueOf("2022-04-05"), pulledCart.getOrder().getOrderDate(), "Order date should be '2022-04-05'.");
        assertEquals("notes of the order", pulledCart.getOrder().getNote(), "Order notes should match.");

        assertEquals(GameStatus.InStock, pulledCart.getGames().get(0).getGameStatus(), "First game's status should be InStock.");
        assertEquals(GameStatus.OutOfStock, pulledCart.getGames().get(1).getGameStatus(), "Second game's status should be OutOfStock.");
        assertEquals("Halo2", pulledCart.getGames().get(0).getDescription(), "First game should be Halo2.");
        assertEquals("CsGo", pulledCart.getGames().get(1).getDescription(), "Second game should be CsGo.");
        assertEquals(50, pulledCart.getGames().get(0).getPrice(), "First game's price should be 50.");
        assertEquals(15, pulledCart.getGames().get(1).getPrice(), "Second game's price should be 15.");
   
        Customer retrievedCustomer = pulledCart.getOrder().getCustomer();
        assertNotNull(retrievedCustomer, "Customer should not be null.");
        assertEquals("ajn@njr.cm", retrievedCustomer.getEmail(), "Customer's email should be 'ajn@njr.cm'.");
        assertEquals("1234567890", retrievedCustomer.getPhoneNumber(), "Customer's phone number should match.");
        assertEquals("123 street", retrievedCustomer.getAddress(), "Customer's address should match.");
        assertEquals("ajn", retrievedCustomer.getUsername(), "Customer's username should match.");
        assertEquals("ajn2", retrievedCustomer.getPassword(), "Customer's password should match.");

    }



    }