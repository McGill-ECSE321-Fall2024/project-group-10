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
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Platform;

@SpringBootTest
public class PlatformRepositoryTests {
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        platformRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadPlatform() {
       
        String email = "anthony.saber@hotmail.co";
        String username = "AnthonySaber";
        String password = "password";
        int phoneNumber = 1234567890;
        String address = "1234 rue Sainte-Catherine";
    
    
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = accountRepository.save(createdManager);
    
        Platform createdPlatform = new Platform("PS5", createdManager);
        createdPlatform = platformRepository.save(createdPlatform);
        int platformId = createdPlatform.getPlatform_id();

        Platform pulledPlatform = platformRepository.findById(platformId);



        // Assertions
        assertNotNull(pulledPlatform);
        assertNotNull(createdPlatform);
        assertEquals("PS5", pulledPlatform.getPlatformName());
        assertEquals(createdManager.getEmail(), pulledPlatform.getManager().getEmail());
        }

        
        
}








