package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;
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

    // Constants for testing
    private static final int VALID_PROMOTION_ID = 1;
    private static final int INVALID_PROMOTION_ID = 999;
    private static final String VALID_DESCRIPTION = "Summer Sale";
    private static final String UPDATED_DESCRIPTION = "Winter Sale";
    private static final int VALID_DISCOUNT_RATE = 20;
    private static final int UPDATED_DISCOUNT_RATE = 30;
    private static final Date VALID_START_DATE = Date.valueOf("2023-07-01");
    private static final Date VALID_END_DATE = Date.valueOf("2023-07-31");
    private static final Date UPDATED_START_DATE = Date.valueOf("2023-08-01");
    private static final Date UPDATED_END_DATE = Date.valueOf("2023-08-31");
    private static final String VALID_MANAGER_EMAIL = "manager@example.com";
    private static final String INVALID_MANAGER_EMAIL = "invalid@example.com";
    private static final List<Integer> VALID_GAME_IDS = Arrays.asList(1, 2);
    private static final int VALID_GAME_ID = 1;
    private static final int INVALID_GAME_ID = 999;

    // --- Tests for createPromotion ---

    @Test
    public void testCreateValidPromotion() {
        // Arrange
        Manager manager = new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street");
        Game game1 = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(1);
        Game game2 = new Game("Game2", "Description2", 60, GameStatus.InStock, 20, "photoUrl2");
        game2.setGame_id(2);

        when(managerRepository.findByEmail(VALID_MANAGER_EMAIL)).thenReturn(manager);
        when(gameRepository.findById(1)).thenReturn(game1);
        when(gameRepository.findById(2)).thenReturn(game2);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> {
            Promotion savedPromotion = invocation.getArgument(0);
            savedPromotion.setPromotion_id(VALID_PROMOTION_ID);
            return savedPromotion;
        });

        // Act
        Promotion createdPromotion = promotionService.createPromotion(
                VALID_DESCRIPTION,
                VALID_DISCOUNT_RATE,
                VALID_START_DATE,
                VALID_END_DATE,
                VALID_MANAGER_EMAIL,
                VALID_GAME_IDS
        );

        // Assert
        assertNotNull(createdPromotion);
        assertEquals(VALID_PROMOTION_ID, createdPromotion.getPromotion_id());
        assertEquals(VALID_DESCRIPTION, createdPromotion.getDescription());
        assertEquals(VALID_DISCOUNT_RATE, createdPromotion.getDiscountRate());
        assertEquals(VALID_START_DATE, createdPromotion.getStartDate());
        assertEquals(VALID_END_DATE, createdPromotion.getEndDate());
        assertEquals(manager, createdPromotion.getManager());
        assertEquals(2, createdPromotion.getGames().size());
        assertTrue(createdPromotion.getGames().contains(game1));
        assertTrue(createdPromotion.getGames().contains(game2));

        verify(managerRepository, times(1)).findByEmail(VALID_MANAGER_EMAIL);
        verify(gameRepository, times(1)).findById(1);
        verify(gameRepository, times(1)).findById(2);
        verify(promotionRepository, times(1)).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithNullDescription() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    null,
                    VALID_DISCOUNT_RATE,
                    VALID_START_DATE,
                    VALID_END_DATE,
                    VALID_MANAGER_EMAIL,
                    VALID_GAME_IDS
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
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    VALID_DESCRIPTION,
                    -10,
                    VALID_START_DATE,
                    VALID_END_DATE,
                    VALID_MANAGER_EMAIL,
                    VALID_GAME_IDS
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
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    VALID_DESCRIPTION,
                    VALID_DISCOUNT_RATE,
                    null,
                    VALID_END_DATE,
                    VALID_MANAGER_EMAIL,
                    VALID_GAME_IDS
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Start date and end date cannot be null", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithStartDateAfterEndDate() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    VALID_DESCRIPTION,
                    VALID_DISCOUNT_RATE,
                    VALID_END_DATE,
                    VALID_START_DATE,
                    VALID_MANAGER_EMAIL,
                    VALID_GAME_IDS
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Start date cannot be after end date", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithInvalidManagerEmail() {
        // Arrange
        when(managerRepository.findByEmail(INVALID_MANAGER_EMAIL)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    VALID_DESCRIPTION,
                    VALID_DISCOUNT_RATE,
                    VALID_START_DATE,
                    VALID_END_DATE,
                    INVALID_MANAGER_EMAIL,
                    VALID_GAME_IDS
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Manager with email %s not found", INVALID_MANAGER_EMAIL), exception.getMessage());

        verify(managerRepository, times(1)).findByEmail(INVALID_MANAGER_EMAIL);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testCreatePromotionWithNullGameIds() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    VALID_DESCRIPTION,
                    VALID_DISCOUNT_RATE,
                    VALID_START_DATE,
                    VALID_END_DATE,
                    VALID_MANAGER_EMAIL,
                    null
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
        Manager manager = new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street");
        when(managerRepository.findByEmail(VALID_MANAGER_EMAIL)).thenReturn(manager);
        when(gameRepository.findById(1)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.createPromotion(
                    VALID_DESCRIPTION,
                    VALID_DISCOUNT_RATE,
                    VALID_START_DATE,
                    VALID_END_DATE,
                    VALID_MANAGER_EMAIL,
                    Arrays.asList(1)
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game with ID 1 not found", exception.getMessage());

        verify(managerRepository, times(1)).findByEmail(VALID_MANAGER_EMAIL);
        verify(gameRepository, times(1)).findById(1);
        verify(promotionRepository, never()).save(any(Promotion.class));

    }
    // --- Tests for getPromotionById ---


    @Test
    public void testGetPromotionByInvalidId() {
        // Arrange
        when(promotionRepository.findById(INVALID_PROMOTION_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.getPromotionById(INVALID_PROMOTION_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(INVALID_PROMOTION_ID);
    }

    // --- Tests for updatePromotion ---

    @Test
    public void testUpdatePromotion() {
        // Arrange
        Manager manager = new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, manager);
        promotion.setPromotion_id(VALID_PROMOTION_ID);

        Game game1 = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(1);

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);
        when(gameRepository.findById(1)).thenReturn(game1);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Promotion updatedPromotion = promotionService.updatePromotion(
                VALID_PROMOTION_ID,
                UPDATED_DESCRIPTION,
                UPDATED_DISCOUNT_RATE,
                UPDATED_START_DATE,
                UPDATED_END_DATE,
                Arrays.asList(1)
        );

        // Assert
        assertNotNull(updatedPromotion);
        assertEquals(VALID_PROMOTION_ID, updatedPromotion.getPromotion_id());
        assertEquals(UPDATED_DESCRIPTION, updatedPromotion.getDescription());
        assertEquals(UPDATED_DISCOUNT_RATE, updatedPromotion.getDiscountRate());
        assertEquals(UPDATED_START_DATE, updatedPromotion.getStartDate());
        assertEquals(UPDATED_END_DATE, updatedPromotion.getEndDate());
        assertEquals(1, updatedPromotion.getGames().size());
        assertTrue(updatedPromotion.getGames().contains(game1));

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(gameRepository, times(1)).findById(1);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    public void testUpdatePromotionWithInvalidId() {
        // Arrange
        when(promotionRepository.findById(INVALID_PROMOTION_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.updatePromotion(
                    INVALID_PROMOTION_ID,
                    UPDATED_DESCRIPTION,
                    UPDATED_DISCOUNT_RATE,
                    UPDATED_START_DATE,
                    UPDATED_END_DATE,
                    VALID_GAME_IDS
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(INVALID_PROMOTION_ID);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    @Test
    public void testUpdatePromotionWithInvalidDiscountRate() {
        // Arrange
        Manager manager = new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street");
        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, manager);
        promotion.setPromotion_id(VALID_PROMOTION_ID);

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.updatePromotion(
                    VALID_PROMOTION_ID,
                    UPDATED_DESCRIPTION,
                    -10,
                    UPDATED_START_DATE,
                    UPDATED_END_DATE,
                    VALID_GAME_IDS
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Discount rate must be between 0 and 100", exception.getMessage());

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(gameRepository, never()).findById(anyInt());
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    // --- Tests for deletePromotion ---

    @Test
    public void testDeletePromotion() {
        // Arrange
        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);

        // Act
        promotionService.deletePromotion(VALID_PROMOTION_ID);

        // Assert
        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(promotionRepository, times(1)).delete(promotion);
    }

    @Test
    public void testDeletePromotionWithInvalidId() {
        // Arrange
        when(promotionRepository.findById(INVALID_PROMOTION_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.deletePromotion(INVALID_PROMOTION_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(INVALID_PROMOTION_ID);
        verify(promotionRepository, never()).delete(any(Promotion.class));
    }

    // --- Tests for getAllGamesFromPromotion ---

    @Test
    public void testGetAllGamesFromPromotion() {
        // Arrange
        Game game1 = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game1.setGame_id(1);
        Game game2 = new Game("Game2", "Description2", 60, GameStatus.InStock, 20, "photoUrl2");
        game2.setGame_id(2);

        List<Game> games = Arrays.asList(game1, game2);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(games);

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);

        // Act
        List<Game> result = promotionService.getAllGamesFromPromotion(VALID_PROMOTION_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
    }

    @Test
    public void testGetAllGamesFromPromotionWithInvalidId() {
        // Arrange
        when(promotionRepository.findById(INVALID_PROMOTION_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.getAllGamesFromPromotion(INVALID_PROMOTION_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Promotion not found", exception.getMessage());

        verify(promotionRepository, times(1)).findById(INVALID_PROMOTION_ID);
    }

    // --- Tests for getGameByIdFromPromotion ---

    @Test
    public void testGetGameByIdFromPromotion() {
        // Arrange
        Game game = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game.setGame_id(VALID_GAME_ID);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(Arrays.asList(game));

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);

        // Act
        Game result = promotionService.getGameByIdFromPromotion(VALID_PROMOTION_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(result);
        assertEquals(game, result);

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
    }

    @Test
    public void testGetGameByIdFromPromotionGameNotInPromotion() {
        // Arrange
        Game game = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game.setGame_id(VALID_GAME_ID);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(new ArrayList<>());

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            promotionService.getGameByIdFromPromotion(VALID_PROMOTION_ID, VALID_GAME_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Game with ID %d not found in Promotion %d", VALID_GAME_ID, VALID_PROMOTION_ID), exception.getMessage());

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
    }

    // --- Tests for addGameToPromotion ---

    @Test
    public void testAddGameToPromotion() {
        // Arrange
        Game game = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game.setGame_id(VALID_GAME_ID);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(new ArrayList<>());

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Promotion updatedPromotion = promotionService.addGameToPromotion(VALID_PROMOTION_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(updatedPromotion);
        assertTrue(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    public void testAddGameToPromotionGameAlreadyInPromotion() {
        // Arrange
        Game game = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game.setGame_id(VALID_GAME_ID);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(new ArrayList<>(Arrays.asList(game)));

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        // Act
        Promotion updatedPromotion = promotionService.addGameToPromotion(VALID_PROMOTION_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(updatedPromotion);
        assertEquals(1, updatedPromotion.getGames().size());
        assertTrue(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(promotionRepository, never()).save(any(Promotion.class));
    }

    // --- Tests for removeGameFromPromotion ---

    @Test
    public void testRemoveGameFromPromotion() {
        // Arrange
        Game game = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game.setGame_id(VALID_GAME_ID);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(new ArrayList<>(Arrays.asList(game)));

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(promotionRepository.save(any(Promotion.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Promotion updatedPromotion = promotionService.removeGameFromPromotion(VALID_PROMOTION_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(updatedPromotion);
        assertFalse(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(promotionRepository, times(1)).save(promotion);
    }

    @Test
    public void testRemoveGameFromPromotionGameNotInPromotion() {
        // Arrange
        Game game = new Game("Game1", "Description1", 50, GameStatus.InStock, 10, "photoUrl1");
        game.setGame_id(VALID_GAME_ID);

        Promotion promotion = new Promotion(VALID_DESCRIPTION, VALID_DISCOUNT_RATE, VALID_START_DATE, VALID_END_DATE, new Manager(VALID_MANAGER_EMAIL, "managerUser", "managerPass", "123-456-7890", "123 Manager Street"));
        promotion.setPromotion_id(VALID_PROMOTION_ID);
        promotion.setGames(new ArrayList<>());

        when(promotionRepository.findById(VALID_PROMOTION_ID)).thenReturn(promotion);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        // Act
        Promotion updatedPromotion = promotionService.removeGameFromPromotion(VALID_PROMOTION_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(updatedPromotion);
        assertFalse(updatedPromotion.getGames().contains(game));

        verify(promotionRepository, times(1)).findById(VALID_PROMOTION_ID);
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
        verify(promotionRepository, never()).save(any(Promotion.class));
    }
}

