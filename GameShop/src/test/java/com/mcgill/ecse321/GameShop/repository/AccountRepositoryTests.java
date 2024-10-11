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

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;


@SpringBootTest
public class AccountRepositoryTests {
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadCustomerAccount() {
        // create customer1
        String email = "anthony.saber@hotmail.com";
        String username = "AnthonySaber";
        String password = "password";
        int phoneNumber = 1234567890;
        String address = "1234 rue Sainte-Catherine";
        Cart cart = new Cart();
        Customer customer1 = new Customer(email, username, password, phoneNumber, address, cart);

        customer1 = accountRepository.save(customer1);
        Account account2 = accountRepository.findByEmail(email);

        assertNotNull(customer1);
        assertEquals(email, account2.getEmail());
        assertEquals(username, account2.getUsername());
        assertEquals(password, account2.getPassword());
        assertEquals(phoneNumber, account2.getPhoneNumber());
        assertEquals(address, account2.getAddress());
        assertTrue(account2 instanceof Customer, "the account should be a customer");
        assertEquals(cart, ((Customer) account2).getCart());
        
        
}}
