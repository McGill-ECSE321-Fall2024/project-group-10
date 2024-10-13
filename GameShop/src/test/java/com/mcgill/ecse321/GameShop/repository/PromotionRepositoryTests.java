package com.mcgill.ecse321.GameShop.repository;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Promotion;

import jakarta.transaction.Transactional;


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
    @Transactional
    public void testCreateAndReadPromotion() {
        // Create manager
        String email = "nicolas.saade@gmail.com";
        String username = "NicolasSaade";
        String password = "password";
        String phoneNumber = "+1 (438) 865-9297";
        String address = "1234 rue Sainte-Catherine";

        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);
    
        Game createdGame = new Game("Halo", "HAlo", 60, GameStatus.InStock, 100, "https://www.halo.com");
        createdGame = gameRepository.save(createdGame);

        Date startDate = Date.valueOf("2021-10-10");
        Date endDate = Date.valueOf("2021-10-20");

        Promotion createdPromotion = new Promotion("Promotion", 10, startDate, endDate, createdManager);
        createdPromotion.addGame(createdGame);
        createdPromotion = promotionRepository.save(createdPromotion);

        int promotionId = createdPromotion.getPromotion_id();


        Promotion pulledPromotion = promotionRepository.findById(promotionId);

        // Hibernate.initialize(pulledPromotion.getGames());
        List<Game> x = pulledPromotion.getGames();
        Boolean y = pulledPromotion.getGames().contains(createdGame);
        System.out.println(x);
        // Assertions
        assertEquals("Promotion", pulledPromotion.getDescription());
        assertEquals(10, pulledPromotion.getDiscountRate());
        assertEquals(startDate, pulledPromotion.getStartDate());
        assertEquals(endDate, pulledPromotion.getEndDate());
        assertEquals(createdGame.getGame_id(), pulledPromotion.getGames().get(0).getGame_id()); // same issue as the line udner it 
        assertEquals(createdManager.getEmail(), pulledPromotion.getManager().getEmail()); // Be careful with this line, you should compare pk's and not objects
    }

    @Test
    @Transactional
    public void testPromotionWithMultipleGames() {
        // Create manager
        String email = "manager.mohamed@example.com";
        String username = "ManagerUser";
        String password = "password";
        String phoneNumber = "+1 (123) 456-7890";
        String address = "123 Manager St";

        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);

        // Create games
        final Game game1 = gameRepository.save(new Game("Game 1", "Description 1", 50, GameStatus.InStock, 10, "http://example.com/game1.jpg"));
        final Game game2 = gameRepository.save(new Game("Game 2", "Description 2", 60, GameStatus.InStock, 20, "http://example.com/game2.jpg"));

        Date startDate = Date.valueOf("2022-01-01");
        Date endDate = Date.valueOf("2022-01-10");

        Promotion promotion = new Promotion("Multiple Games Promotion", 15, startDate, endDate, createdManager);
        promotion.addGame(game1);
        promotion.addGame(game2);
        promotion = promotionRepository.save(promotion);

        // Retrieve promotion
        Promotion pulledPromotion = promotionRepository.findById(promotion.getPromotion_id());

        // Assertions
        assertNotNull(pulledPromotion);
        assertEquals(2, pulledPromotion.getGames().size(), "Promotion should have 2 games.");
        assertTrue(pulledPromotion.getGames().stream().anyMatch(game -> game.getGame_id() == game1.getGame_id()), "Promotion should contain Game 1.");
        assertTrue(pulledPromotion.getGames().stream().anyMatch(game -> game.getGame_id() == game2.getGame_id()), "Promotion should contain Game 2.");
    }

    @Test
    @Transactional
    public void testOverlappingPromotionsForSameGame() {
        // Create manager
        String email = "manager.mohamed.abdelhady@example2.com";
        String username = "ManagerUser";
        String password = "password";
        String phoneNumber = "+1 (123) 456-7890";
        String address = "123 Manager St";

        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);

        // Create game
        Game createdGame = new Game("Overlapping Promotion Game", "Description", 50, GameStatus.InStock, 10, "http://example.com/game.jpg");
        createdGame = gameRepository.save(createdGame);

        // Create first promotion
        Date startDate1 = Date.valueOf("2022-01-01");
        Date endDate1 = Date.valueOf("2022-01-10");
        Promotion firstPromotion = new Promotion("First Promotion", 10, startDate1, endDate1, createdManager);
        firstPromotion.addGame(createdGame);
        firstPromotion = promotionRepository.save(firstPromotion);

        // Create second promotion with overlapping dates
        Date startDate2 = Date.valueOf("2022-01-05");
        Date endDate2 = Date.valueOf("2022-01-15");
        Promotion secondPromotion = new Promotion("Second Promotion", 20, startDate2, endDate2, createdManager);
        secondPromotion.addGame(createdGame);
        secondPromotion = promotionRepository.save(secondPromotion);

        // Retrieve promotions
        Promotion pulledFirstPromotion = promotionRepository.findById(firstPromotion.getPromotion_id());
        Promotion pulledSecondPromotion = promotionRepository.findById(secondPromotion.getPromotion_id());

        // Assertions
        assertNotNull(pulledFirstPromotion, "First promotion should exist.");
        assertNotNull(pulledSecondPromotion, "Second promotion should exist.");

        // Check that both promotions contain the same game
        assertEquals(createdGame.getGame_id(), pulledFirstPromotion.getGames().get(0).getGame_id());
        assertEquals(createdGame.getGame_id(), pulledSecondPromotion.getGames().get(0).getGame_id());
    }

}