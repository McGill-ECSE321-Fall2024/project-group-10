package com.mcgill.ecse321.GameShop.repository;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Manager;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ManagerRepositoryTests {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        managerRepository.deleteAll();
        cartRepository.deleteAll();
       
    }

    @Test
    @Transactional
        public void testCreateAndReadManagerAccountAsAccount() {
            // Create Manager
            String email = "mohamed@hotmail.com";
            String username = "AnthonySaber";
            String password = "password";
            String phoneNumber = "+1 (438) 865-9293";
            String address = "1234 rue Sainte-Catherine";



            Manager createdManager = new Manager(email, username, password, phoneNumber, address);
            createdManager = managerRepository.save(createdManager);
            
            Account pulledManager = managerRepository.findByEmail(email);

            assertNotNull(createdManager);
            assertEquals(email, pulledManager.getEmail());
            assertEquals(username, pulledManager.getUsername());
            assertEquals(password, pulledManager.getPassword());
            assertEquals(phoneNumber, pulledManager.getPhoneNumber());
            assertEquals(address, pulledManager.getAddress());
            assertTrue(pulledManager instanceof Manager, "The account should be a manager.");
            List<Account> managers = (List<Account>) managerRepository.findAll();

            // Assertions
            assertNotNull(managers);
            assertEquals(1, managers.size());
        }
}
