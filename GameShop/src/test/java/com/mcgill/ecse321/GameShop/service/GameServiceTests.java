package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class  GameServiceTests {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PlatformRepository platformRepository;
    @Mock
    private ManagerRepository managerRepository;
    @InjectMocks
    private GameService gameService;

    private static int VALID_GAME_ID = 77;
    private static int INVALID_GAME_ID = -1;
    private static String VALID_TITLE = "Valid Title";
    private static String VALID_DESCRIPTION = "Valid Description";
    private static int VALID_PRICE = 100;
    private static int VALID_STOCK_QUANTITY = 10;
    private static String VALID_PHOTO_URL = "Valid Photo URL";
    private static GameStatus VALID_GAME_STATUS = GameStatus.InStock;
    private static Manager VALID_MANAGER = new Manager("manageremailofgame@example.com","usernameof maneger","ManagerPassqoesd","154142365","montreal");
    private static String VALID_MANAGER_EMAIL = VALID_MANAGER.getEmail();
    @Test
    public void testCreateGame() {
        // when(managerRepository.findByEmail(VALID_MANAGER_EMAIL)).thenReturn(VALID_MANAGER);


        when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> {
            Game savedGame = invocation.getArgument(0);
            savedGame.setGame_id(VALID_GAME_ID);
            return savedGame;
        });
        Game createdGame = gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        assertNotNull(createdGame);
        assertEquals(VALID_GAME_ID, createdGame.getGame_id());
        assertEquals(VALID_TITLE, createdGame.getTitle());
        assertEquals(VALID_DESCRIPTION, createdGame.getDescription());
        assertEquals(VALID_PRICE, createdGame.getPrice());
        assertEquals(VALID_GAME_STATUS, createdGame.getGameStatus());
        assertEquals(VALID_STOCK_QUANTITY, createdGame.getStockQuantity());
        assertEquals(VALID_PHOTO_URL, createdGame.getPhotoUrl());
        verify(gameRepository, times(1)).save(any(Game.class));}
    
    @Test
    public void testCreateGameNullTitle() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(null, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Title cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }
    @Test
    public void testCreateGameEmptyTitle(){
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(" ", VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Title cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameNullDescription(){
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, null, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameEmptyDescription(){
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, " ", VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));}
        @Test
        public void testCreateGameNegativePrice() {
            GameShopException exception = assertThrows(GameShopException.class, () -> {
                gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, -10, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
            });
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Price cannot be negative or 0", exception.getMessage());
            verify(gameRepository, times(0)).save(any(Game.class));
        }
     @Test
    public void testCreateGameZeroPrice() {
        // Assuming price cannot be zero
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, 0, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Price cannot be negative or 0", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }
    @Test
    public void testCreateGameNullGameStatus() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, null, VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game status cannot be null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }
    @Test
    public void testCreateGameNegativeStockQuantity() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, -5, VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Stock quantity cannot be negative", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }
    @Test
    public void testCreateGameNullPhotoUrl() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Photo URL cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameEmptyPhotoUrl(){
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY, " ");
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Photo URL cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }




    
    
    
    
    
    
    }
        
   

       



    

