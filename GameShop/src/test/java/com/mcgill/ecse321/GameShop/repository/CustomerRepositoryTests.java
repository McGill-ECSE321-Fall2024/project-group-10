package com.mcgill.ecse321.GameShop.repository;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;

@SpringBootTest
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        customerRepository.deleteAll();
        cartRepository.deleteAll();
       
    }

    @Test
    public void testCreateAndReadCustomerAccountAsAccount() {
        // create customer1
        String email = "anthony.saber@hotmail";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9294";
        String address = "1234 rue Sainte-Catherine";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        int cartId = cart.getCart_id();

        Customer customer1 = new Customer(email, username, password, phoneNumber, address, cart);
        customer1 = customerRepository.save(customer1);
        
        Account account2 = customerRepository.findByEmail(email);

        assertNotNull(customer1);
        assertEquals(email, account2.getEmail());
        assertEquals(username, account2.getUsername());
        assertEquals(password, account2.getPassword());
        assertEquals(phoneNumber, account2.getPhoneNumber());
        assertEquals(address, account2.getAddress());
        assertTrue(account2 instanceof Customer, "The account should be a customer.");
        assertEquals(cartId, ((Customer)account2).getCart().getCart_id());  
}
}
