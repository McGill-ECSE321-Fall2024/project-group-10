package com.mcgill.ecse321.GameShop.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Promotion;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PromotionRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class PromotionServiceTests {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private PromotionService promotionService;

    // --- Tests for createPromotion ---

    @Test
    public void testCreateValidPromotion() {
        // Arrange
        int promotionId = 1;
        String managerEmail = "manager1@example.com";
        String description = "Summer Sale";
        int discountRate = 20;
        LocalDate startDate = LocalDate.parse("2023-07-01");
        LocalDate endDate = LocalDate.parse("2023-07-31");
        List<Integer> gameIds = Arrays.asList(10001, 10002);

        Manager manager = new Manager(managerEmail, "managerUser1", "managerPass1", "123-456-7890",
                "123 Manager Street");
        Game game1 = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(10001);
        Game game2 = new Game("Game2", "Description2", 60, GameStatus.InStock, 20, "photoUrl2");
        game2.setGame_id(10002);

        when(managerRepository.findByEmail(managerEmail)).thenReturn(manager);
        when(gameRepository.findById(10001)).thenReturn(game1);
        when(gameRepository.findById(10002)).thenReturn(game2);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> {
            Promotion savedPromotion = invocation.getArgument(0);
            savedPromotion.setPromotion_id(promotionId);
            return savedPromotion;
        });

        // Act
        Promotion createdPromotion = promotionService.createPromotion(
                description,
                discountRate,
                startDate,
                endDate,
                managerEmail,
                gameIds
        );

        // Assert
        assertNotNull(createdPromotion);
        assertEquals(promotionId, createdPromotion.getPromotion_id());
        assertEquals(description, createdPromotion.getDescription());
        assertEquals(discountRate, createdPromotion.getDiscountRate());
        assertEquals(startDate, createdPromotion.getStartLocalDate());
        assertEquals(endDate, createdPromotion.getEndLocalDate());
        assertEquals(manager, createdPromotion.getManager());
        assertEquals(2, createdPromotion.getGames().size());
        assertTrue(createdPromotion.getGames().contains(game1));
        assertTrue(createdPromotion.getGames().contains(game2));

        verify(managerRepository, times(1)).findByEmail(managerEmail);
        verify(gameRepository, times(1)).findById(10001);
        verify(gameRepository, times(1)).findById(10002);
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithNullDescription() {
        // Arrange
        int promotionId = 2;
        String managerEmail = "manager2@example.com";
        String description = null;
        int discountRate = 20;
        LocalDate startDate = LocalDate.parse("2023-07-01");
        LocalDate endDate = LocalDate.parse("2023-07-31");
        List<Integer> gameIds = Arrays.asList(103, 104);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    managerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithInvalidDiscountRate() {
        // Arrange
        int promotionId = 3;
        String managerEmail = "manager3@example.com";
        String description = "Autumn Sale";
        int discountRate = -10; // Invalid
        LocalDate startDate = LocalDate.parse("2023-09-01");
        LocalDate endDate = LocalDate.parse("2023-09-30");
        List<Integer> gameIds = Arrays.asList(105, 106);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    managerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Discount rate must be between 0 and 100", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithNullStartDate() {
        // Arrange
        int promotionId = 4;
        String managerEmail = "manager4@example.com";
        String description = "Winter Sale";
        int discountRate = 20;
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.parse("2023-12-31");
        List<Integer> gameIds = Arrays.asList(107, 108);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    managerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Start LocalDate and end LocalDate cannot be null", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithStartDateAfterEndDate() {
        // Arrange
        int promotionId = 5;
        String managerEmail = "manager5@example.com";
        String description = "Flash Sale";
        int discountRate = 25;
        LocalDate startDate = LocalDate.parse("2023-10-01");
        LocalDate endDate = LocalDate.parse("2023-09-30"); // End LocalDate before start LocalDate
        List<Integer> gameIds = Arrays.asList(109, 110);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    managerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Start LocalDate cannot be after end LocalDate", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithInvalidManagerEmail() {
        // Arrange
        String invalidManagerEmail = "invalidmanager@example.com";
        String description = "Summer Sale";
        int discountRate = 20;
        LocalDate startDate = LocalDate.parse("2023-07-01");
        LocalDate endDate = LocalDate.parse("2023-07-31");
        List<Integer> gameIds = Arrays.asList(111, 112);

        when(managerRepository.findByEmail(invalidManagerEmail)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    invalidManagerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Manager with email %s not found", invalidManagerEmail), exception.getMessage());

        verify(managerRepository, times(1)).findByEmail(invalidManagerEmail);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithNullGameIds() {
        // Arrange
        int promotionId = 6;
        String managerEmail = "manager6@example.com";
        String description = "Black Friday Sale";
        int discountRate = 30;
        LocalDate startDate = LocalDate.parse("2023-11-25");
        LocalDate endDate = LocalDate.parse("2023-11-30");
        List<Integer> gameIds = null;

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    managerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game IDs cannot be null or empty", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithNonexistentGameId() {
        // Arrange
        int promotionId = 7;
        String managerEmail = "manager7@example.com";
        String description = "Cyber Monday Sale";
        int discountRate = 35;
        LocalDate startDate = LocalDate.parse("2023-11-27");
        LocalDate endDate = LocalDate.parse("2023-12-02");
        List<Integer> gameIds = Arrays.asList(113); // Game ID 113 does not exist

        Manager manager = new Manager(managerEmail, "managerUser7", "managerPass7", "123-456-7890", "123 Manager Street");
        when(managerRepository.findByEmail(managerEmail)).thenReturn(manager);
        when(gameRepository.findById(113)).thenReturn(null); // Nonexistent game

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    description,
                    discountRate,
                    startDate,
                    endDate,
                    managerEmail,
                    gameIds
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game with ID 113 not found", exception.getMessage());

        verify(managerRepository, times(1)).findByEmail(managerEmail);
        verify(gameRepository, times(1)).findById(113);
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    // --- Tests for getPromotionById ---

    @Test
    public void testGetPromotionByInvalidId() {
        // Arrange
        int invalidPromotionId = 999;
        when(promotionRepository.findById(invalidPromotionId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.getPromotionById(invalidPromotionId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(invalidPromotionId);
    }

    @Test
    public void testGetPromotionByIdValid() {
        // Arrange
        int promotionId = 1004;
        String managerEmail = "manager1004@example.com";
        Manager manager = new Manager(managerEmail, "managerUser1004", "managerPass1004", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion("Promo1004", 15, LocalDate.parse("2023-03-01"), LocalDate.parse("2023-03-31"), manager);
        promotion.setPromotion_id(promotionId);

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);

        // Act
        Promotion retrievedPromotion = promotionService.getPromotionById(promotionId);

        // Assert
        assertNotNull(retrievedPromotion);
        assertEquals(promotionId, retrievedPromotion.getPromotion_id());
        assertEquals("Promo1004", retrievedPromotion.getDescription());
        assertEquals(manager, retrievedPromotion.getManager());
        verify(promotionRepository, times(1)).findById(promotionId);
    }

    // --- Tests for updatePromotion ---

    @Test
    public void testUpdatePromotion() {
        // Arrange
        int promotionId = 8;
        String managerEmail = "manager8@example.com";
        String description = "Holiday Sale";
        int discountRate = 40;
        LocalDate startDate = LocalDate.parse("2024-12-01");
        LocalDate endDate = LocalDate.parse("2024-12-31");
        List<Integer> gameIds = Arrays.asList(114);

        String updatedDescription = "New Year Sale";
        int updatedDiscountRate = 50;
        LocalDate updatedStartDate = LocalDate.parse("2024-01-01");
        LocalDate updatedEndDate = LocalDate.parse("2024-01-31");
        List<Integer> updatedGameIds = Arrays.asList(114);

        Manager manager = new Manager(managerEmail, "managerUser8", "managerPass8", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        Game game1 = new Game("Game3", "Description3", 70, GameStatus.InStock, 15, "photoUrl3");
        game1.setGame_id(114);
        promotion.setGames(new ArrayList<>(Arrays.asList(game1))); // Use mutable list

        Game game2 = new Game("Game4", "Description4", 80, GameStatus.InStock, 25, "photoUrl4");
        game2.setGame_id(115);

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);
        when(gameRepository.findById(114)).thenReturn(game1);

        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Promotion updatedPromotion = promotionService.updatePromotion(
                promotionId,
                updatedDescription,
                updatedDiscountRate,
                updatedStartDate,
                updatedEndDate,
                updatedGameIds
        );

        // Assert
        assertNotNull(updatedPromotion);
        assertEquals(promotionId, updatedPromotion.getPromotion_id());
        assertEquals(updatedDescription, updatedPromotion.getDescription());
        assertEquals(updatedDiscountRate, updatedPromotion.getDiscountRate());
        assertEquals(updatedStartDate, updatedPromotion.getStartLocalDate());
        assertEquals(updatedEndDate, updatedPromotion.getEndLocalDate());
        assertEquals(1, updatedPromotion.getGames().size());
        assertTrue(updatedPromotion.getGames().contains(game1));

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, times(1)).findById(114);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    public void testUpdatePromotionWithInvalidId() {
        // Arrange
        int invalidPromotionId = 1000;
        String updatedDescription = "New Year Sale";
        int updatedDiscountRate = 50;
        LocalDate updatedStartDate = LocalDate.parse("2024-01-01");
        LocalDate updatedEndDate = LocalDate.parse("2024-01-31");
        List<Integer> updatedGameIds = Arrays.asList(116);

        when(promotionRepository.findById(invalidPromotionId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.updatePromotion(
                    invalidPromotionId,
                    updatedDescription,
                    updatedDiscountRate,
                    updatedStartDate,
                    updatedEndDate,
                    updatedGameIds
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(invalidPromotionId);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testUpdatePromotionWithInvalidDiscountRate() {
        // Arrange
        int promotionId = 9;
        String managerEmail = "manager9@example.com";
        String description = "Spring Sale";
        int discountRate = 15;
        LocalDate startDate = LocalDate.parse("2024-03-01");
        LocalDate endDate = LocalDate.parse("2024-03-31");
        List<Integer> gameIds = Arrays.asList(117);

        String updatedDescription = "Updated Spring Sale";
        int invalidDiscountRate = -5;
        LocalDate updatedStartDate = LocalDate.parse("2024-04-01");
        LocalDate updatedEndDate = LocalDate.parse("2024-04-30");
        List<Integer> updatedGameIds = Arrays.asList(117);

        Manager manager = new Manager(managerEmail, "managerUser9", "managerPass9", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        Game game1 = new Game("Game5", "Description5", 90, GameStatus.InStock, 35, "photoUrl5");
        game1.setGame_id(117);
        promotion.setGames(Arrays.asList(game1));

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.updatePromotion(
                    promotionId,
                    updatedDescription,
                    invalidDiscountRate,
                    updatedStartDate,
                    updatedEndDate,
                    updatedGameIds
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Discount rate must be between 0 and 100", exception.getMessage());

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    // --- Tests for deletePromotion ---

    @Test
    public void testDeletePromotion() {
        // Arrange
        int promotionId = 10;
        String managerEmail = "manager10@example.com";
        String description = "Mega Sale";
        int discountRate = 50;
        LocalDate startDate = LocalDate.parse("2024-05-01");
        LocalDate endDate = LocalDate.parse("2024-05-31");
        List<Integer> gameIds = Arrays.asList(118, 119);

        Manager manager = new Manager(managerEmail, "managerUser10", "managerPass10", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        Game game1 = new Game("Game6", "Description6", 100, GameStatus.InStock, 50, "photoUrl6");
        game1.setGame_id(118);
        Game game2 = new Game("Game7", "Description7", 110, GameStatus.InStock, 60, "photoUrl7");
        game2.setGame_id(119);
        promotion.setGames(Arrays.asList(game1, game2));

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);

        // Act
        promotionService.deletePromotion(promotionId);

        // Assert
        verify(promotionRepository, times(1)).findById(promotionId);
        verify(promotionRepository, times(1)).delete(promotion);
    }

    @Test
    public void testDeletePromotionWithInvalidId() {
        // Arrange
        int invalidPromotionId = 1001;
        when(promotionRepository.findById(invalidPromotionId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.deletePromotion(invalidPromotionId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(invalidPromotionId);
        verify(promotionRepository, never()).delete(any(Promotion.class));
    }

    // --- Tests for getAllPromotions ---

    @Test
    public void testGetAllPromotionsEmpty() {
        // Arrange
        when(promotionRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        Iterable<Promotion> promotions = promotionService.getAllPromotions();

        // Assert
        assertNotNull(promotions);
        assertFalse(promotions.iterator().hasNext());
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllPromotionsNonEmpty() {
        // Arrange
        int promotionId1 = 2001;
        int promotionId2 = 2002;
        Manager manager = new Manager("managerAll@example.com", "managerAll", "passAll", "123-456-7890", "123 All Street");
        Promotion promotion1 = new Promotion("Promo1", 10, LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-31"), manager);
        promotion1.setPromotion_id(promotionId1);
        Promotion promotion2 = new Promotion("Promo2", 20, LocalDate.parse("2023-02-01"), LocalDate.parse("2023-02-28"), manager);
        promotion2.setPromotion_id(promotionId2);
        List<Promotion> promotionList = Arrays.asList(promotion1, promotion2);

        when(promotionRepository.findAll()).thenReturn(promotionList);

        // Act
        Iterable<Promotion> promotions = promotionService.getAllPromotions();

        // Assert
        assertNotNull(promotions);
        List<Promotion> resultList = new ArrayList<>();
        promotions.forEach(resultList::add);
        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(promotion1));
        assertTrue(resultList.contains(promotion2));
        verify(promotionRepository, times(1)).findAll();
    }


    // --- Tests for getAllGamesFromPromotion ---

    @Test
    public void testGetAllGamesFromPromotion() {
        // Arrange
        int promotionId = 11;
        String managerEmail = "manager11@example.com";
        String description = "Holiday Sale";
        int discountRate = 45;
        LocalDate startDate = LocalDate.parse("2024-12-01");
        LocalDate endDate = LocalDate.parse("2024-12-31");
        List<Integer> gameIds = Arrays.asList(120, 121);

        Manager manager = new Manager(managerEmail, "managerUser11", "managerPass11", "123-456-7890", "123 Manager Street");
        Game game1 = new Game("Game8", "Description8", 120, GameStatus.InStock, 70, "photoUrl8");
        game1.setGame_id(120);
        Game game2 = new Game("Game9", "Description9", 130, GameStatus.InStock, 80, "photoUrl9");
        game2.setGame_id(121);

        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(Arrays.asList(game1, game2));

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);

        // Act
        List<Game> result = promotionService.getAllGamesFromPromotion(promotionId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));

        verify(promotionRepository, times(1)).findById(promotionId);
    }

    @Test
    public void testGetAllGamesFromPromotionWithInvalidId() {
        // Arrange
        int invalidPromotionId = 1002;
        when(promotionRepository.findById(invalidPromotionId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.getAllGamesFromPromotion(invalidPromotionId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(invalidPromotionId);
    }

    // --- Tests for getGameByIdFromPromotion ---

    @Test
    public void testGetGameByIdFromPromotion() {
        // Arrange
        int promotionId = 12;
        String managerEmail = "manager12@example.com";
        String description = "Clearance Sale";
        int discountRate = 60;
        LocalDate startDate = LocalDate.parse("2024-06-01");
        LocalDate endDate = LocalDate.parse("2024-06-30");
        List<Integer> gameIds = Arrays.asList(122);

        Game game = new Game("Game10", "Description10", 140, GameStatus.InStock, 90, "photoUrl10");
        game.setGame_id(122);

        Manager manager = new Manager(managerEmail, "managerUser12", "managerPass12", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(Arrays.asList(game));

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);

        // Act
        Game result = promotionService.getGameByIdFromPromotion(promotionId, 122);

        // Assert
        assertNotNull(result);
        assertEquals(game, result);

        verify(promotionRepository, times(1)).findById(promotionId);
    }

    @Test
    public void testGetGameByIdFromPromotionGameNotInPromotion() {
        // Arrange
        int promotionId = 13;
        String managerEmail = "manager13@example.com";
        String description = "Exclusive Sale";
        int discountRate = 55;
        LocalDate startDate = LocalDate.parse("2024-07-01");
        LocalDate endDate = LocalDate.parse("2024-07-31");
        List<Integer> gameIds = Arrays.asList(123);

        Manager manager = new Manager(managerEmail, "managerUser13", "managerPass13", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(new ArrayList<>()); // No games in promotion

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);

        // Act & Assert
        int gameId = 123;
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.getGameByIdFromPromotion(promotionId, gameId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Game with ID %d not found in Promotion %d", gameId, promotionId), exception.getMessage());

        verify(promotionRepository, times(1)).findById(promotionId);
    }

    // --- Tests for addGameToPromotion ---

    @Test
    public void testAddGameToPromotion() {
        // Arrange
        int promotionId = 14;
        String managerEmail = "manager14@example.com";
        String description = "New Product Launch Sale";
        int discountRate = 25;
        LocalDate startDate = LocalDate.parse("2024-09-01");
        LocalDate endDate = LocalDate.parse("2024-09-30");
        List<Integer> gameIds = Arrays.asList(124);

        Game game = new Game("Game11", "Description11", 150, GameStatus.InStock, 100, "photoUrl11");
        game.setGame_id(124);

        Manager manager = new Manager(managerEmail, "managerUser14", "managerPass14", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(new ArrayList<>()); // Empty game list

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);
        when(gameRepository.findById(124)).thenReturn(game);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Promotion updatedPromotion = promotionService.addGameToPromotion(promotionId, 124);

        // Assert
        assertNotNull(updatedPromotion);
        assertTrue(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, times(1)).findById(124);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    public void testAddGameToPromotionGameAlreadyInPromotion() {
        // Arrange
        int promotionId = 15;
        String managerEmail = "manager15@example.com";
        String description = "Limited Time Offer";
        int discountRate = 35;
        LocalDate startDate = LocalDate.parse("2024-10-01");
        LocalDate endDate = LocalDate.parse("2024-10-31");
        List<Integer> gameIds = Arrays.asList(125);

        Game game = new Game("Game12", "Description12", 160, GameStatus.InStock, 110, "photoUrl12");
        game.setGame_id(125);

        Manager manager = new Manager(managerEmail, "managerUser15", "managerPass15", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(new ArrayList<>(Arrays.asList(game))); // Game already in promotion

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);
        when(gameRepository.findById(125)).thenReturn(game);

        // Act
        Promotion updatedPromotion = promotionService.addGameToPromotion(promotionId, 125);

        // Assert
        assertNotNull(updatedPromotion);
        assertEquals(1, updatedPromotion.getGames().size());
        assertTrue(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, times(1)).findById(125);
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testAddGameToPromotionPromotionNotFound() {
        // Arrange
        int promotionId = 100;
        int gameId = 200;
        when(promotionRepository.findById(promotionId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.addGameToPromotion(promotionId, gameId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    // --- Tests for removeGameFromPromotion ---

    @Test
    public void testRemoveGameFromPromotion() {
        // Arrange
        int promotionId = 16;
        String managerEmail = "manager16@example.com";
        String description = "Flash Discount";
        int discountRate = 40;
        LocalDate startDate = LocalDate.parse("2024-11-01");
        LocalDate endDate = LocalDate.parse("2024-11-30");
        List<Integer> gameIds = Arrays.asList(126);

        Game game = new Game("Game13", "Description13", 170, GameStatus.InStock, 120, "photoUrl13");
        game.setGame_id(126);

        Manager manager = new Manager(managerEmail, "managerUser16", "managerPass16", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(new ArrayList<>(Arrays.asList(game))); // Game in promotion

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);
        when(gameRepository.findById(126)).thenReturn(game);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Promotion updatedPromotion = promotionService.removeGameFromPromotion(promotionId, 126);

        // Assert
        assertNotNull(updatedPromotion);
        assertFalse(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, times(1)).findById(126);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    public void testRemoveGameFromPromotionPromotionNotFound() {
        // Arrange
        int promotionId = 102;
        int gameId = 202;
        when(promotionRepository.findById(promotionId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.removeGameFromPromotion(promotionId, gameId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testRemoveGameFromPromotionGameNotInPromotion() {
        // Arrange
        int promotionId = 17;
        String managerEmail = "manager17@example.com";
        String description = "Mega Discount";
        int discountRate = 50;
        LocalDate startDate = LocalDate.parse("2024-12-01");
        LocalDate endDate = LocalDate.parse("2024-12-31");
        List<Integer> gameIds = Arrays.asList(127);

        Game game = new Game("Game14", "Description14", 180, GameStatus.InStock, 130, "photoUrl14");
        game.setGame_id(127);

        Manager manager = new Manager(managerEmail, "managerUser17", "managerPass17", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setPromotion_id(promotionId);
        promotion.setGames(new ArrayList<>()); // No games in promotion

        when(promotionRepository.findById(promotionId)).thenReturn(promotion);
        when(gameRepository.findById(127)).thenReturn(game);

        // Act
        Promotion updatedPromotion = promotionService.removeGameFromPromotion(promotionId, 127);

        // Assert
        assertNotNull(updatedPromotion);
        assertFalse(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(promotionId);
        verify(gameRepository, times(1)).findById(127);
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
}
