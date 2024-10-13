package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.SpecificGame;

import jakarta.transaction.Transactional;

@SpringBootTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SpecificGameRepository specificgameRepository;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        specificgameRepository.deleteAll();
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    @Transactional
    public void TestCreateAndReadOrder(){
        String email = "14@14.com";
        String username = "JudeSousou";
        String password = "p123";
        String phoneNumber = "+1 (438) 320-239";
        String address = "1110 rue Sainte-Catherine";

        
        // Creating Cart
        Cart cart = new Cart();
        cart = cartRepository.save(cart);


        String aDescription = "Halo2";
        int aPrice = 50;
        GameStatus aGameStatus = GameStatus.InStock;
        int aStockQuantity = 10;
        String aPhotoUrl = "www.halo2.com";

        Game firstGame = new Game(aDescription + "A", aDescription, aPrice, aGameStatus, aStockQuantity, aPhotoUrl);
        firstGame = gameRepository.save(firstGame);

        SpecificGame firstSpecificGame = new SpecificGame(firstGame);
        firstSpecificGame = specificgameRepository.save(firstSpecificGame);


        // Creating Customer
        Customer firstCustomer = new Customer(email, username, password, phoneNumber, address, cart);
        firstCustomer = customerRepository.save(firstCustomer);
        
        // Creating date, note, and paymentCard in firstOrder to define firstOrder
        Date orderDate = Date.valueOf("2021-10-10");
        String note = "testing";
        int paymentCard = 1209028310;

        // Defining firstOrder and saving it to the firstOrder table
        Order firstOrder = new Order(orderDate, note, paymentCard, firstCustomer);
        firstOrder = orderRepository.save(firstOrder);
        firstSpecificGame.addOrder(firstOrder);
        firstSpecificGame = specificgameRepository.save(firstSpecificGame);
        SpecificGame pulledFirstSpecificGame = specificgameRepository.findById(firstSpecificGame.getSpecificGame_id());
        cart.addGame(firstGame);
        cart = cartRepository.save(cart);

        


        
        // Getting the tracking number from the firstOrder
        String firstTrackingNumber = firstOrder.getTrackingNumber();

                // Creating date, note, and paymentCard in firstOrder to define firstOrder
        Date orderDate2 = Date.valueOf("2021-10-12");
        String note2 = "testingorder2";
        int paymentCard2 = 12090;

        // Defining firstOrder and saving it to the firstOrder table
        Order secondOrder = new Order(orderDate2, note2, paymentCard2, firstCustomer);
        secondOrder = orderRepository.save(secondOrder);


        
        // Getting the tracking number from the firstOrder
        String secondTrackingNumber = secondOrder.getTrackingNumber();
        

        Order firstPulledOrder = orderRepository.findByTrackingNumber(firstTrackingNumber);
        Order secondPulledOrder = orderRepository.findByTrackingNumber(secondTrackingNumber);

        assertNotNull(firstPulledOrder);
        assertEquals(orderDate, firstPulledOrder.getOrderDate());
        assertEquals(firstOrder.getTrackingNumber(), firstPulledOrder.getTrackingNumber());
        assertEquals(note, firstPulledOrder.getNote());
        assertEquals(paymentCard, firstPulledOrder.getPaymentCard());
        assertEquals(email, firstPulledOrder.getCustomer().getEmail());
        assertEquals(username, firstPulledOrder.getCustomer().getUsername());
        assertEquals(password, firstPulledOrder.getCustomer().getPassword());
        assertEquals(phoneNumber, firstPulledOrder.getCustomer().getPhoneNumber());
        assertEquals(address, firstPulledOrder.getCustomer().getAddress());
        assertEquals(firstGame.getGame_id(), firstPulledOrder.getCustomer().getCart().getGames().getFirst().getGame_id());
        String firstOrderTrackingNumber = firstOrder.getTrackingNumber();
        boolean foundOrder = pulledFirstSpecificGame.getOrder().stream().anyMatch(order -> order.getTrackingNumber() == firstOrderTrackingNumber);
        assertTrue(foundOrder, "First order should be in the specific game."); 

        assertNotNull(secondPulledOrder);
        assertEquals(orderDate2, secondPulledOrder.getOrderDate());
        assertEquals(secondOrder.getTrackingNumber(), secondPulledOrder.getTrackingNumber());
        assertEquals(note2, secondPulledOrder.getNote());
        assertEquals(paymentCard2, secondPulledOrder.getPaymentCard());
        assertEquals(email, secondPulledOrder.getCustomer().getEmail());
        assertEquals(username, secondPulledOrder.getCustomer().getUsername());
        assertEquals(password, secondPulledOrder.getCustomer().getPassword());
        assertEquals(phoneNumber, secondPulledOrder.getCustomer().getPhoneNumber());
        assertEquals(address, secondPulledOrder.getCustomer().getAddress());

        List<Order> orders = (List<Order>) orderRepository.findAll();
        assertEquals(2, orders.size());
    }
    
}
