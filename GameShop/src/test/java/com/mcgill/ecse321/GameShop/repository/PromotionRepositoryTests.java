package com.mcgill.ecse321.GameShop.repository;
import java.sql.Date;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Promotion;


@SpringBootTest
public class PromotionRepositoryTests {
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AccountRepository managerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        promotionRepository.deleteAll();
        managerRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadPromotion() {
        // Create manager
        String email = "nicolas.saade@gmail.com";
        String username = "NicolasSaade";
        String password = "password";
        int phoneNumber = 1234567890;
        String address = "1234 rue Sainte-Catherine";

        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);
    
        Game createdGame = new Game("Halo", 60, GameStatus.InStock, 100, "https://www.halo.com"); // TODO Add title field
        createdGame = gameRepository.save(createdGame);

        Date startDate = Date.valueOf("2021-10-10");
        Date endDate = Date.valueOf("2021-10-20");

        Promotion createdPromotion = new Promotion("Promotion", 10, startDate, endDate, createdManager);
        createdPromotion.addGame(createdGame);
        createdPromotion = promotionRepository.save(createdPromotion);

        int promotionId = createdPromotion.getPromotion_id();


        Promotion pulledPromotion = promotionRepository.findById(promotionId);

        Hibernate.initialize(pulledPromotion.getGames());
        
        // Assertions
        assertEquals("Promotion", pulledPromotion.getDescription());
        assertEquals(10, pulledPromotion.getDiscountRate());
        assertEquals(startDate, pulledPromotion.getStartDate());
        assertEquals(endDate, pulledPromotion.getEndDate());
        assertTrue(pulledPromotion.getGames().contains(createdGame));       
        assertEquals(createdManager.getEmail(), pulledPromotion.getManager().getEmail()); // Be careful with this line, you should compare pk's and not objects
    }
}