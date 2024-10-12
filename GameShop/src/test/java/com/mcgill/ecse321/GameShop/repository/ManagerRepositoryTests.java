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
import com.mcgill.ecse321.GameShop.model.Manager;

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
        public void testCreateAndReadManagerAccountAsAccount() {
            // Create Manager
            String email = "anthony.saber.as@hotmail.commmmmm";
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
            
        }
}
