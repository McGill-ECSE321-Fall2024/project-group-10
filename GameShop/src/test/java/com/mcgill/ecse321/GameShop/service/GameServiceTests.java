package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.GlyphView;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Category;
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
public class GameServiceTests {

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

    private static int VALID_GAME_ID;
    private static int INVALID_GAME_ID = 799;
    private static int INVALID_GAME_ID2 = -1;
    private static String VALID_TITLE = "Valid Title";
    private static String VALID_DESCRIPTION = "Valid Description";
    private static int VALID_PRICE = 100;
    private static int VALID_STOCK_QUANTITY = 10;
    private static String VALID_PHOTO_URL = "Valid Photo URL";
    private static GameStatus VALID_GAME_STATUS = GameStatus.InStock;
    private static Manager VALID_MANAGER = new Manager("manageremailofgame@example.com", "usernameof maneger",
            "ManagerPassqoesd", "154142365", "montreal");
    private static String VALID_MANAGER_EMAIL = VALID_MANAGER.getEmail();

    @Test
    public void testCreateGame() {
        VALID_GAME_ID = 1;
        // when(managerRepository.findByEmail(VALID_MANAGER_EMAIL)).thenReturn(VALID_MANAGER);

        when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> {
            Game savedGame = invocation.getArgument(0);
            savedGame.setGame_id(VALID_GAME_ID);
            return savedGame;
        });
        Game createdGame = gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS,
                VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        assertNotNull(createdGame);
        assertEquals(VALID_GAME_ID, createdGame.getGame_id(), "Game ID is not the same");
        assertEquals(VALID_TITLE, createdGame.getTitle(), "Title is not the same");
        assertEquals(VALID_DESCRIPTION, createdGame.getDescription(), "Description is not the same");
        assertEquals(VALID_PRICE, createdGame.getPrice(), "Price is not the same");
        assertEquals(VALID_GAME_STATUS, createdGame.getGameStatus(), "Game status is not the same");
        assertEquals(VALID_STOCK_QUANTITY, createdGame.getStockQuantity(), "Stock quantity is not the same");
        assertEquals(VALID_PHOTO_URL, createdGame.getPhotoUrl(), "Photo URL is not the same");
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void testCreateGameNullTitle() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(null, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Title cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameEmptyTitle() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(" ", VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Title cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameNullDescription() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, null, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameEmptyDescription() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, " ", VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameNegativePrice() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, -10, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Price cannot be negative or 0", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameZeroPrice() {
        // Assuming price cannot be zero
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, 0, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Price cannot be negative or 0", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameNullGameStatus() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, null, VALID_STOCK_QUANTITY,
                    VALID_PHOTO_URL);
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
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Photo URL cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testCreateGameEmptyPhotoUrl() {
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS, VALID_STOCK_QUANTITY,
                    " ");
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Photo URL cannot be empty or null", exception.getMessage());
        verify(gameRepository, times(0)).save(any(Game.class));
    }

    @Test
    public void testFindGameById() {
        VALID_GAME_ID = 2;
        Game createdGame = gameService.createGame(VALID_TITLE, VALID_DESCRIPTION, VALID_PRICE, VALID_GAME_STATUS,
                VALID_STOCK_QUANTITY, VALID_PHOTO_URL);
        createdGame.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(createdGame);

        Game foundGame = gameService.findGameById(VALID_GAME_ID);
        assertNotNull(foundGame);
        assertEquals(VALID_GAME_ID, foundGame.getGame_id());
        assertEquals(VALID_TITLE, foundGame.getTitle());
        assertEquals(VALID_DESCRIPTION, foundGame.getDescription());
        assertEquals(VALID_PRICE, foundGame.getPrice());
        assertEquals(VALID_GAME_STATUS, foundGame.getGameStatus());
        assertEquals(VALID_STOCK_QUANTITY, foundGame.getStockQuantity());
        assertEquals(VALID_PHOTO_URL, foundGame.getPhotoUrl());
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
    }

    @Test
    public void testFindGameByInvalidId() {
        when(gameRepository.findById(INVALID_GAME_ID)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.findGameById(INVALID_GAME_ID);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game with ID 799 does not exist", exception.getMessage());
        verify(gameRepository, times(1)).findById(INVALID_GAME_ID);
    }

    @Test
    public void testFindGameByInvalidIdValue() {

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.findGameById(INVALID_GAME_ID2);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game ID must be greater than 0", exception.getMessage());
    }

    @Test
    public void testGetAllGames() {
        // Arrange
        VALID_GAME_ID = 3;
        int  SECOND_GAME_ID = 4;
        Game game1 = new Game("Game Title 1", "Description 1", 30, Game.GameStatus.InStock, 100,
                "http://example.com/image1.jpg");
        game1.setGame_id(VALID_GAME_ID);
        Game game2 = new Game("Game Title 2", "Description 2", 50, Game.GameStatus.OutOfStock, 200,
                "http://example.com/image2.jpg");
        game2.setGame_id(SECOND_GAME_ID);

        List<Game> gamesArray = Arrays.asList(game1, game2);
        when(gameRepository.findAll()).thenReturn(gamesArray);

        // Act
        Iterable<Game> games = gameService.getAllGames();

        // Assert
        assertNotNull(games);
        List<Game> gameList = new ArrayList<>();
        games.forEach(gameList::add);
        assertTrue(gameList.contains(game1));
        assertTrue(gameList.contains(game2));
        assertEquals(2, gameList.size());

        // Assertions for Game 1
        assertEquals(VALID_GAME_ID, gameList.get(0).getGame_id());
        assertEquals("Game Title 1", gameList.get(0).getTitle());
        assertEquals("Description 1", gameList.get(0).getDescription());
        assertEquals(30, gameList.get(0).getPrice());
        assertEquals(Game.GameStatus.InStock, gameList.get(0).getGameStatus());
        assertEquals(100, gameList.get(0).getStockQuantity());
        assertEquals("http://example.com/image1.jpg", gameList.get(0).getPhotoUrl());

        // Assertions for Game 2
        assertEquals(SECOND_GAME_ID, gameList.get(1).getGame_id());
        assertEquals("Game Title 2", gameList.get(1).getTitle());
        assertEquals("Description 2", gameList.get(1).getDescription());
        assertEquals(50, gameList.get(1).getPrice());
        assertEquals(Game.GameStatus.OutOfStock, gameList.get(1).getGameStatus());
        assertEquals(200, gameList.get(1).getStockQuantity());
        assertEquals("http://example.com/image2.jpg", gameList.get(1).getPhotoUrl());

        verify(gameRepository, times(1)).findAll();
    }

    // update tests in the following section
    @Test
    public void testUpdateGameTitle() {
        VALID_GAME_ID = 5;
        Game game = new Game("Old Title", "Description", 30, Game.GameStatus.InStock, 100,
                "http://example.com/image1.jpg");
        game.setGame_id(VALID_GAME_ID);

        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(gameRepository.save(any(Game.class)))
                .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        Game updatedGame = gameService.updateGameTitle(VALID_GAME_ID, "New Title");

        assertNotNull(updatedGame);
        assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
        assertEquals("New Title", updatedGame.getTitle());
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
    }

    @Test
    public void testUpdateGameTitleWithInvalidId() {
        int invalidId = 9877;
        when(gameRepository.findById(invalidId)).thenReturn(null);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGameTitle(invalidId, "New Title");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game with ID 9877 does not exist", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
        verify(gameRepository, times(1)).findById(invalidId);
    }

    @Test
    public void testUpdateGameTitleWithNullTitle() {
        VALID_GAME_ID = 6;
        Game game = new Game("Old Title", "Description", 30, Game.GameStatus.InStock, 100,
                "http://example.com/image1.jpg");
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGameTitle(VALID_GAME_ID, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Title cannot be empty or null", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    public void testUpdateGameTitleWithEmptyTitle() {
        VALID_GAME_ID = 8;
        Game game = new Game("Old Title", "Description", 30, Game.GameStatus.InStock, 100,
                "http://example.com/image1.jpg");
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

        GameShopException exception = assertThrows(GameShopException.class, () -> {
            gameService.updateGameTitle(VALID_GAME_ID, "  ");
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Title cannot be empty or null", exception.getMessage());
        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
public void testUpdateGameDescription() {
    VALID_GAME_ID = 9;
    Game game = new Game("Game Title", "Old Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);

    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    Game updatedGame = gameService.updateGameDescription(VALID_GAME_ID, "New Description");

    assertNotNull(updatedGame);
    assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
    assertEquals("New Description", updatedGame.getDescription());
    verify(gameRepository, times(1)).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

@Test
public void testUpdateGameDescriptionWithNullDescription() {
    VALID_GAME_ID = 10;
    Game game = new Game("Game Title", "Old Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateGameDescription(VALID_GAME_ID, null);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Description cannot be empty or null", exception.getMessage());
    verify(gameRepository, never()).save(any(Game.class));
}

@Test
public void testUpdateGameDescriptionWithEmptyDescription() {
    VALID_GAME_ID = 11;
    Game game = new Game("Game Title", "Old Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateGameDescription(VALID_GAME_ID, "  ");
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Description cannot be empty or null", exception.getMessage());
    verify(gameRepository, never()).save(any(Game.class));
}

@Test
public void testUpdateGameStockQuantity() {
    VALID_GAME_ID = 12;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);

    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    Game updatedGame = gameService.updateGameStockQuantity(VALID_GAME_ID, 200);

    assertNotNull(updatedGame);
    assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
    assertEquals(200, updatedGame.getStockQuantity());
    verify(gameRepository, times(1)).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

@Test
public void testUpdateGameStockQuantityWithNegativeValue() {
    VALID_GAME_ID = 13;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);
    
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateGameStockQuantity(VALID_GAME_ID, -1);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Stock quantity cannot be negative", exception.getMessage());
    verify(gameRepository, never()).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}
@Test
public void testUpdateGameStatus() {
    VALID_GAME_ID = 14;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);

    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    Game updatedGame = gameService.updateGameStatus(VALID_GAME_ID, Game.GameStatus.Archived);

    assertNotNull(updatedGame);
    assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
    assertEquals(Game.GameStatus.Archived, updatedGame.getGameStatus());
    verify(gameRepository, times(1)).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

@Test
public void testUpdateGameStatusWithNullStatus() {
    VALID_GAME_ID = 15;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);
    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);

    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateGameStatus(VALID_GAME_ID, null);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Game status cannot be null", exception.getMessage());
    verify(gameRepository, never()).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

@Test
public void testUpdateGamePhotoUrl() {
    VALID_GAME_ID = 16;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/oldImage.jpg");
    game.setGame_id(VALID_GAME_ID);

    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    Game updatedGame = gameService.updateGamePhotoUrl(VALID_GAME_ID, "http://example.com/newImage.jpg");

    assertNotNull(updatedGame);
    assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
    assertEquals("http://example.com/newImage.jpg", updatedGame.getPhotoUrl());
    verify(gameRepository, times(1)).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

@Test
public void testUpdateGamePhotoUrlWithNullPhotoUrl() {
    VALID_GAME_ID = 17;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/oldImage.jpg");
    game.setGame_id(VALID_GAME_ID);
    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateGamePhotoUrl(VALID_GAME_ID, null);
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Photo URL cannot be empty or null", exception.getMessage());
    verify(gameRepository, never()).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

@Test
public void testUpdateGamePhotoUrlWithEmptyPhotoUrl() {
    VALID_GAME_ID = 18;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/oldImage.jpg");
    game.setGame_id(VALID_GAME_ID);
    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateGamePhotoUrl(VALID_GAME_ID, "  ");
    });

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Photo URL cannot be empty or null", exception.getMessage());
    verify(gameRepository, never()).save(any(Game.class));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
}

// add update and delete catgeory and platfrom tests
// @Test
// public void testAddCategoryToGame_Successful() {
//     VALID_GAME_ID = 19;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Category category = new Category("Category Name", VALID_MANAGER);
//     category.setCategory_id(1013);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(categoryRepository.findById(1013)).thenReturn(category);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.setCategory(VALID_GAME_ID, 1013);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getCategories().contains(category));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(categoryService, times(1)).getCategory(1013);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testAddCategoryToGame_CategoryNotFound() {
//     VALID_GAME_ID = 20;
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.addCategoryToGame(VALID_GAME_ID, 1014);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 50 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(categoryService, never()).getCategory(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testAddPlatformToGame_Successful() {
//     VALID_GAME_ID = 21;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Platform platform = new Platform();
//     platform.setPlatformId(2010);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(platformService.getPlatform(2010)).thenReturn(platform);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.addPlatformToGame(VALID_GAME_ID, 2010);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getPlatforms().contains(platform));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, times(1)).getPlatform(2010);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testAddPlatformToGame_PlatformNotFound() {
//     VALID_GAME_ID = 22;
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.addPlatformToGame(VALID_GAME_ID, 2011);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 52 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, never()).getPlatform(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }
@Test
public void testUpdateCategories_Successful() {
    VALID_GAME_ID = 23;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);
    
    Category category1 = new Category("Category 1", VALID_MANAGER);
    category1.setCategory_id(1001);
    Category category2 = new Category("Category 2", VALID_MANAGER);
    category2.setCategory_id(1002);
    
    List<Integer> categoryIds = Arrays.asList(1001, 1002);
    
    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    when(categoryRepository.findById(1001)).thenReturn(category1);
    when(categoryRepository.findById(1002)).thenReturn(category2);

    when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
    Game updatedGame = gameService.updateCategories(game.getGame_id(), categoryIds);
    
    assertNotNull(updatedGame);
    assertTrue(updatedGame.getCategories().contains(category1));
    assertTrue(updatedGame.getCategories().contains(category2));
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
    verify(gameRepository, times(1)).save(game);
}

@Test
public void testUpdateCategories_NullCategories() {
    VALID_GAME_ID = 24;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);
    // when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);  
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateCategories(VALID_GAME_ID, null);
    });
    
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    assertEquals("Categories cannot be null", exception.getMessage());
    verify(gameRepository, never()).findById(anyInt());
    verify(categoryRepository, never()).findById(anyInt());
    verify(gameRepository, never()).save(any(Game.class));
}

@Test
public void testUpdateCategories_CategoryNotFound() {
    VALID_GAME_ID = 25;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    List<Integer> categoryIds = Arrays.asList(1003);
    
    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    
    GameShopException exception = assertThrows(GameShopException.class, () -> {
        gameService.updateCategories(VALID_GAME_ID, categoryIds);
    });
    
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    assertEquals("Category does not exist", exception.getMessage());
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
    verify(gameRepository, never()).save(any(Game.class));
}}

@Test
public void testUpdateCategories_AddDuplicateCategories() {
    VALID_GAME_ID = 26;
    Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
    game.setGame_id(VALID_GAME_ID);
    
    Category category1 = new Category("Category 1", VALID_MANAGER);
    category1.setCategory_id(1004);
    
    game.addCategory(category1);
    
    List<Integer> categoryIds = Arrays.asList(1004);
    
    when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    when(categoryService.getCategory(1004)).thenReturn(category1);
    when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
    Game updatedGame = gameService.updateCategories(VALID_GAME_ID, categoryIds);
    
    assertNotNull(updatedGame);
    assertEquals(1, updatedGame.getCategories().size());
    verify(gameRepository, times(1)).findById(VALID_GAME_ID);
    verify(categoryService, times(1)).getCategory(1004);
    verify(gameRepository, times(1)).save(game);
}

// @Test
// public void testUpdatePlatforms_Successful() {
//     VALID_GAME_ID = 23;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Platform platform1 = new Platform();
//     platform1.setPlatformId(2001);
//     Platform platform2 = new Platform();
//     platform2.setPlatformId(2002);
    
//     List<Integer> platformIds = Arrays.asList(2001, 2002);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(platformService.getPlatform(2001)).thenReturn(platform1);
//     when(platformService.getPlatform(2002)).thenReturn(platform2);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updatePlatforms(VALID_GAME_ID, platformIds);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getPlatforms().contains(platform1));
//     assertTrue(updatedGame.getPlatforms().contains(platform2));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, times(1)).getPlatform(2001);
//     verify(platformService, times(1)).getPlatform(2002);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdatePlatforms_NullPlatforms() {
//     VALID_GAME_ID = 24;
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updatePlatforms(VALID_GAME_ID, null);
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Platforms cannot be null", exception.getMessage());
//     verify(gameRepository, never()).findById(anyInt());
//     verify(platformService, never()).getPlatform(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdatePlatforms_PlatformNotFound() {
//     VALID_GAME_ID = 25;
//     List<Integer> platformIds = Arrays.asList(2003);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updatePlatforms(VALID_GAME_ID, platformIds);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 25 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, never()).getPlatform(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdatePlatforms_AddDuplicatePlatforms() {
//     VALID_GAME_ID = 26;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Platform platform1 = new Platform();
//     platform1.setPlatformId(2004);
    
//     game.addPlatform(platform1);
    
//     List<Integer> platformIds = Arrays.asList(2004);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(platformService.getPlatform(2004)).thenReturn(platform1);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updatePlatforms(VALID_GAME_ID, platformIds);
    
//     assertNotNull(updatedGame);
//     assertEquals(1, updatedGame.getPlatforms().size());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, times(1)).getPlatform(2004);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGamePrice_Successful() {
//     VALID_GAME_ID = 27;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateGamePrice(VALID_GAME_ID, 60);
    
//     assertNotNull(updatedGame);
//     assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
//     assertEquals(60, updatedGame.getPrice());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGamePrice_NegativePrice() {
//     VALID_GAME_ID = 28;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateGamePrice(VALID_GAME_ID, -20);
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Price cannot be negative", exception.getMessage());
//     verify(gameRepository, never()).save(any(Game.class));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
// }

// @Test
// public void testUpdateGameStatus_Successful() {
//     VALID_GAME_ID = 29;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateGameStatus(VALID_GAME_ID, Game.GameStatus.Archived);
    
//     assertNotNull(updatedGame);
//     assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
//     assertEquals(Game.GameStatus.Archived, updatedGame.getGameStatus());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGameStatus_NullStatus() {
//     VALID_GAME_ID = 30;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image1.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateGameStatus(VALID_GAME_ID, null);
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Game status cannot be null", exception.getMessage());
//     verify(gameRepository, never()).save(any(Game.class));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
// }

// @Test
// public void testUpdateGamePhotoUrl_Successful() {
//     VALID_GAME_ID = 31;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/oldImage.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateGamePhotoUrl(VALID_GAME_ID, "http://example.com/newImage.jpg");
    
//     assertNotNull(updatedGame);
//     assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
//     assertEquals("http://example.com/newImage.jpg", updatedGame.getPhotoUrl());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGamePhotoUrl_NullPhotoUrl() {
//     VALID_GAME_ID = 32;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/oldImage.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateGamePhotoUrl(VALID_GAME_ID, null);
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Photo URL cannot be empty or null", exception.getMessage());
//     verify(gameRepository, never()).save(any(Game.class));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
// }

// @Test
// public void testUpdateGamePhotoUrl_EmptyPhotoUrl() {
//     VALID_GAME_ID = 33;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/oldImage.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateGamePhotoUrl(VALID_GAME_ID, "  ");
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Photo URL cannot be empty or null", exception.getMessage());
//     verify(gameRepository, never()).save(any(Game.class));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
// }

// @Test
// public void testUpdateGameTitle_DuplicateTitle() {
//     VALID_GAME_ID = 34;
//     Game game = new Game("Original Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateGameTitle(VALID_GAME_ID, "Original Title");
    
//     assertNotNull(updatedGame);
//     assertEquals("Original Title", updatedGame.getTitle());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGamePrice_ZeroPrice() {
//     VALID_GAME_ID = 35;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateGamePrice(VALID_GAME_ID, 0);
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Price cannot be negative", exception.getMessage());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdateGameStockQuantity_Successful() {
//     VALID_GAME_ID = 36;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateGameStockQuantity(VALID_GAME_ID, 150);
    
//     assertNotNull(updatedGame);
//     assertEquals(VALID_GAME_ID, updatedGame.getGame_id());
//     assertEquals(150, updatedGame.getStockQuantity());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGameStockQuantity_InvalidId() {
//     VALID_GAME_ID = 37;
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateGameStockQuantity(VALID_GAME_ID, 50);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 37 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdateGamePlatforms_Successful() {
//     VALID_GAME_ID = 38;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Platform platform1 = new Platform();
//     platform1.setPlatformId(2005);
//     Platform platform2 = new Platform();
//     platform2.setPlatformId(2006);
    
//     List<Integer> platformIds = Arrays.asList(2005, 2006);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(platformService.getPlatform(2005)).thenReturn(platform1);
//     when(platformService.getPlatform(2006)).thenReturn(platform2);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updatePlatforms(VALID_GAME_ID, platformIds);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getPlatforms().contains(platform1));
//     assertTrue(updatedGame.getPlatforms().contains(platform2));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, times(1)).getPlatform(2005);
//     verify(platformService, times(1)).getPlatform(2006);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGamePlatforms_PlatformNotFound() {
//     VALID_GAME_ID = 39;
//     List<Integer> platformIds = Arrays.asList(2007);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updatePlatforms(VALID_GAME_ID, platformIds);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 39 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, never()).getPlatform(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdateGamePlatforms_AddDuplicatePlatforms() {
//     VALID_GAME_ID = 40;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Platform platform1 = new Platform();
//     platform1.setPlatformId(2008);
    
//     game.addPlatform(platform1);
    
//     List<Integer> platformIds = Arrays.asList(2008);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(platformService.getPlatform(2008)).thenReturn(platform1);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updatePlatforms(VALID_GAME_ID, platformIds);
    
//     assertNotNull(updatedGame);
//     assertEquals(1, updatedGame.getPlatforms().size());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, times(1)).getPlatform(2008);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGamePlatforms_NullPlatforms() {
//     VALID_GAME_ID = 41;
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updatePlatforms(VALID_GAME_ID, null);
//     });
    
//     assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
//     assertEquals("Platforms cannot be null", exception.getMessage());
//     verify(gameRepository, never()).findById(anyInt());
//     verify(platformService, never()).getPlatform(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdateGamePlatforms_EmptyPlatforms() {
//     VALID_GAME_ID = 42;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     List<Integer> platformIds = new ArrayList<>();
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updatePlatforms(VALID_GAME_ID, platformIds);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getPlatforms().isEmpty());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGameCategories_Successful() {
//     VALID_GAME_ID = 43;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Category category1 = new Category();
//     category1.setCategoryId(1009);
//     Category category2 = new Category();
//     category2.setCategoryId(1010);
    
//     List<Integer> categoryIds = Arrays.asList(1009, 1010);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(categoryService.getCategory(1009)).thenReturn(category1);
//     when(categoryService.getCategory(1010)).thenReturn(category2);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateCategories(VALID_GAME_ID, categoryIds);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getCategories().contains(category1));
//     assertTrue(updatedGame.getCategories().contains(category2));
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(categoryService, times(1)).getCategory(1009);
//     verify(categoryService, times(1)).getCategory(1010);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGameCategories_EmptyCategories() {
//     VALID_GAME_ID = 44;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     List<Integer> categoryIds = new ArrayList<>();
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateCategories(VALID_GAME_ID, categoryIds);
    
//     assertNotNull(updatedGame);
//     assertTrue(updatedGame.getCategories().isEmpty());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdateGameCategories_PlatformNotFound() {
//     VALID_GAME_ID = 45;
//     List<Integer> categoryIds = Arrays.asList(1011);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updateCategories(VALID_GAME_ID, categoryIds);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 45 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(categoryService, never()).getCategory(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdateGameCategories_AddDuplicateCategories() {
//     VALID_GAME_ID = 46;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Category category1 = new Category();
//     category1.setCategoryId(1012);
    
//     game.addCategory(category1);
    
//     List<Integer> categoryIds = Arrays.asList(1012);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(categoryService.getCategory(1012)).thenReturn(category1);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updateCategories(VALID_GAME_ID, categoryIds);
    
//     assertNotNull(updatedGame);
//     assertEquals(1, updatedGame.getCategories().size());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(categoryService, times(1)).getCategory(1012);
//     verify(gameRepository, times(1)).save(game);
// }

// @Test
// public void testUpdatePlatforms_InvalidPlatformId() {
//     VALID_GAME_ID = 47;
//     List<Integer> platformIds = Arrays.asList(9999);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
    
//     GameShopException exception = assertThrows(GameShopException.class, () -> {
//         gameService.updatePlatforms(VALID_GAME_ID, platformIds);
//     });
    
//     assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
//     assertEquals("Game with ID 47 does not exist", exception.getMessage());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, never()).getPlatform(anyInt());
//     verify(gameRepository, never()).save(any(Game.class));
// }

// @Test
// public void testUpdatePlatforms_AddExistingPlatform() {
//     VALID_GAME_ID = 48;
//     Game game = new Game("Game Title", "Description", 30, Game.GameStatus.InStock, 100, "http://example.com/image.jpg");
//     game.setGame_id(VALID_GAME_ID);
    
//     Platform platform1 = new Platform();
//     platform1.setPlatformId(2009);
    
//     game.addPlatform(platform1);
    
//     List<Integer> platformIds = Arrays.asList(2009);
    
//     when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
//     when(platformService.getPlatform(2009)).thenReturn(platform1);
//     when(gameRepository.save(any(Game.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
    
//     Game updatedGame = gameService.updatePlatforms(VALID_GAME_ID, platformIds);
    
//     assertNotNull(updatedGame);
//     assertEquals(1, updatedGame.getPlatforms().size());
//     verify(gameRepository, times(1)).findById(VALID_GAME_ID);
//     verify(platformService, times(1)).getPlatform(2009);
//     verify(gameRepository, times(1)).save(game);
// }






// }
