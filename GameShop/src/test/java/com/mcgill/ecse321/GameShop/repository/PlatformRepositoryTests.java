package com.mcgill.ecse321.GameShop.repository;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

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
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void testCreateAndReadPlatform() {
       
        String email = "anthony.saber@hotmail.cox";
        String username = "AnthonySaber";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9290";
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








