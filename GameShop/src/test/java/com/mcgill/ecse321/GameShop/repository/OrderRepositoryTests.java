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
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Order;

@SpringBootTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AccountRepository accountRepository;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();  
    }

    @Test
    public void TestCreateAndReadOrder(){
        String email = "judes@gmail.com";
        String username = "JudeSousou";
        String password = "p123";
        String phoneNumber = "+1 (438) 320-239";
        String address = "1110 rue Sainte-Catherine";
        
        // Creating Cart
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        int cartId = cart.getCart_id();

        // Creating Customer
        Customer customer1 = new Customer(email, username, password, phoneNumber, address, cart);
        customer1 = accountRepository.save(customer1);
        
        // Creating date, note, and paymentCard in order to define order
        Date orderDate = Date.valueOf("2021-10-10");
        String note = "testing";
        int paymentCard = 1209028310;

        // Defining order and saving it to the order table
        Order order = new Order(orderDate, note, paymentCard, customer1);
        order = orderRepository.save(order);
        
        // Getting the tracking number from the order
        String trackingNumber = order.getTrackingNumber();
        

        Order orderFromDb = orderRepository.findByTrackingNumber(trackingNumber);

        assertNotNull(orderFromDb);
        assertEquals(orderDate, orderFromDb.getOrderDate());
        assertEquals(note, orderFromDb.getNote());
        assertEquals(paymentCard, orderFromDb.getPaymentCard());
        assertEquals(email, orderFromDb.getCustomer().getEmail());
        assertEquals(username, orderFromDb.getCustomer().getUsername());
        assertEquals(password, orderFromDb.getCustomer().getPassword());
        assertEquals(phoneNumber, orderFromDb.getCustomer().getPhoneNumber());
        assertEquals(address, orderFromDb.getCustomer().getAddress());
    }
    
}
