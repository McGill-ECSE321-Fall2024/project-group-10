package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.model.SpecificGame.ItemStatus;
import com.mcgill.ecse321.GameShop.repository.SpecificGameRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class SpecificGameServiceTests {

    private static List<Integer> testGames = new ArrayList<>(List.of(6));

    @Mock
    private SpecificGameRepository specificGameRepository;

    @Mock
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private SpecificGameService specificGameService;

    private static  int VALID_SPECIFIC_GAME_ID = 1;
    private static  int INVALID_SPECIFIC_GAME_ID = 999;
    private static  int INVALID_SPECIFIC_GAME_ID_NEGATIVE = -1;

    private static  int VALID_GAME_ID = 10;
    private static  int INVALID_GAME_ID = 888;
    private static  int INVALID_GAME_ID_NEGATIVE = -10;

    private static  Game VALID_GAME = new Game("Valid Game", "A valid game description", 50, Game.GameStatus.InStock, 100, "http://example.com/game.jpg");

    // --- Tests for createSpecificGame ---

    @BeforeAll
    @AfterAll
    public static void clearTestIds()
    {
        Game.clearTestEmails(testGames); // Add all test game ids to this list
        SpecificGameServiceTests.testGames.clear();
    }

    @Test
    public void testCreateSpecificGame_Successful() {
        VALID_SPECIFIC_GAME_ID = 1041;
        // Arrange
        when(specificGameRepository.save(any(SpecificGame.class))).thenAnswer((InvocationOnMock invocation) -> {
            SpecificGame savedSpecificGame = invocation.getArgument(0);
            savedSpecificGame.setSpecificGame_id(VALID_SPECIFIC_GAME_ID);
            return savedSpecificGame;
        });

        // Act
        SpecificGame createdSpecificGame = specificGameService.createSpecificGame(VALID_GAME);

        // Assert
        assertNotNull(createdSpecificGame);
        assertEquals(VALID_SPECIFIC_GAME_ID, createdSpecificGame.getSpecificGame_id());
        assertEquals(VALID_GAME, createdSpecificGame.getGames());
        verify(specificGameRepository, times(1)).save(any(SpecificGame.class));
    }

    @Test
    public void testCreateSpecificGame_NullGame() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.createSpecificGame(null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game cannot be null", exception.getMessage());
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }

    // // --- Tests for findSpecificGameById ---

    @Test
    public void testFindSpecificGameById_Successful() {
        VALID_SPECIFIC_GAME_ID = 2;
        // Arrange
        SpecificGame specificGame = new SpecificGame(VALID_GAME);
        specificGame.setSpecificGame_id(VALID_SPECIFIC_GAME_ID);

        when(specificGameRepository.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(specificGame);

        // Act
        SpecificGame foundSpecificGame = specificGameService.findSpecificGameById(VALID_SPECIFIC_GAME_ID);

        // Assert
        assertNotNull(foundSpecificGame);
        assertEquals(VALID_SPECIFIC_GAME_ID, foundSpecificGame.getSpecificGame_id());
        assertEquals(VALID_GAME, foundSpecificGame.getGames());
        verify(specificGameRepository, times(1)).findById(VALID_SPECIFIC_GAME_ID);
    }

    @Test
    public void testFindSpecificGameByInvalidId_NotFound() {
        // Arrange
        when(specificGameRepository.findById(INVALID_SPECIFIC_GAME_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.findSpecificGameById(INVALID_SPECIFIC_GAME_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("SpecificGame does not exist", exception.getMessage());
        verify(specificGameRepository, times(1)).findById(INVALID_SPECIFIC_GAME_ID);
    }

    @Test
    public void testFindSpecificGameById_InvalidIdValue() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.findSpecificGameById(INVALID_SPECIFIC_GAME_ID_NEGATIVE);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("SpecificGame ID must be greater than 0", exception.getMessage());
        verify(specificGameRepository, never()).findById(anyInt());
    }


    // // --- Tests for updateSpecificGame ---

    @Test
    public void testUpdateSpecificGame_Successful() {
        VALID_SPECIFIC_GAME_ID = 3;
        VALID_GAME_ID = 3;
        // Arrange
        SpecificGame specificGame = new SpecificGame(VALID_GAME);
        specificGame.setSpecificGame_id(VALID_SPECIFIC_GAME_ID);

        Game newGame = new Game("New Game", "New description", 60, Game.GameStatus.InStock, 50, "http://example.com/newgame.jpg");
        newGame.setGame_id(VALID_GAME_ID);

        when(specificGameRepository.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(specificGame);
        when(gameService.findGameById(VALID_GAME_ID)).thenReturn(newGame);
        when(specificGameRepository.save(any(SpecificGame.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        SpecificGame updatedGame = specificGameService.updateSpecificGame(VALID_SPECIFIC_GAME_ID, VALID_GAME_ID);

        // Assert
        assertNotNull(updatedGame);
        assertEquals(newGame, specificGame.getGames());
        verify(specificGameRepository, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(gameService, times(1)).findGameById(VALID_GAME_ID);
        verify(specificGameRepository, times(1)).save(specificGame);
    }

    @Test
    public void testUpdateSpecificGame_NonExistentSpecificGameId() {
        VALID_GAME_ID = 4;
        VALID_GAME.setGame_id(VALID_GAME_ID);
        INVALID_SPECIFIC_GAME_ID = 999;
        // Arrange
        when(specificGameRepository.findById(INVALID_SPECIFIC_GAME_ID)).thenReturn(null);
        // when(gameService.findGameById(VALID_GAME_ID)).thenReturn(VALID_GAME);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGame(INVALID_SPECIFIC_GAME_ID, VALID_GAME_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("SpecificGame does not exist", exception.getMessage());
        verify(specificGameRepository, times(1)).findById(INVALID_SPECIFIC_GAME_ID);
        verify(gameService, never()).findGameById(anyInt());
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }

    @Test
    public void testUpdateSpecificGame_NonExistentGameId() {
        // Arrange
        SpecificGame specificGame = new SpecificGame(VALID_GAME);
        specificGame.setSpecificGame_id(VALID_SPECIFIC_GAME_ID);

        when(specificGameRepository.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(specificGame);
        when(gameService.findGameById(INVALID_GAME_ID)).thenReturn(null);
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGame(VALID_SPECIFIC_GAME_ID, INVALID_GAME_ID);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game does not exist", exception.getMessage());
        verify(specificGameRepository, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(gameService, times(1)).findGameById(INVALID_GAME_ID);
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }

    @Test
    public void testUpdateSpecificGame_InvalidIds() {
        // Act & Assert
        GameShopException exception1 = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGame(INVALID_SPECIFIC_GAME_ID_NEGATIVE, VALID_GAME_ID);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception1.getStatus());
        assertEquals("SpecificGame ID must be greater than 0", exception1.getMessage());

        GameShopException exception2 = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGame(VALID_SPECIFIC_GAME_ID, INVALID_GAME_ID_NEGATIVE);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception2.getStatus());
        assertEquals("Game ID must be greater than 0", exception2.getMessage());

        verify(specificGameRepository, never()).findById(anyInt());
        verify(gameService, never()).findGameById(anyInt());
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }

    // // --- Tests for getSpecificGamesByGameId ---


@Test
public void testGetSpecificGamesByGameId_Successful() {
    // Arrange
    int VALID_GAME_ID = 6; // Ensure this is consistent with the ID in the service method
    Game game = new Game("Test Game", "Test Description", 40, Game.GameStatus.InStock, 20, "http://example.com/testgame.jpg");
    game.setGame_id(VALID_GAME_ID);
    assertEquals(6, game.getGame_id());

    SpecificGame specificGame1 = new SpecificGame(game);
    specificGame1.setSpecificGame_id(106);
    SpecificGame specificGame2 = new SpecificGame(game);
    specificGame2.setSpecificGame_id(107);

    Iterable<SpecificGame> allSpecificGames = Arrays.asList(specificGame1, specificGame2);

    when(specificGameRepository.findAll()).thenReturn(allSpecificGames);
    when(gameRepository.findById(6)).thenReturn(game);


    // Act
    Iterable<SpecificGame> specificGames = specificGameService.getSpecificGamesByGameId(VALID_GAME_ID);


    // Assert
    List<SpecificGame> specificGameList = new ArrayList<>();
    specificGames.forEach(specificGameList::add);

    assertEquals(2, specificGameList.size());
    assertTrue(specificGameList.contains(specificGame1));
    assertTrue(specificGameList.contains(specificGame2));
    verify(specificGameRepository, times(1)).findAll();
}

    @Test
    public void testGetSpecificGamesByGameId_NoSpecificGames() {
        // Arrange
        when(specificGameRepository.findAll()).thenReturn(new ArrayList<>());
        int VALID_GAME_ID = 101; // Ensure this is consistent with the ID in the service method
        VALID_GAME.setGame_id(VALID_GAME_ID);
        assertEquals(VALID_GAME_ID, VALID_GAME.getGame_id());
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(VALID_GAME);
        GameShopException exception1 = assertThrows(GameShopException.class, () -> {
            specificGameService.getSpecificGamesByGameId(VALID_GAME_ID);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception1.getStatus());
        assertEquals("No SpecificGame found at all", exception1.getMessage());
        verify(specificGameRepository, times(1)).findAll();
    }

    @Test
    public void testGetSpecificGamesByGameId_NoGameFound() {
        // Arrange
        // when(specificGameRepository.findAll()).thenReturn(new ArrayList<>());
        int VALID_GAME_ID = 10103; // Ensure this is consistent with the ID in the service method
        VALID_GAME.setGame_id(VALID_GAME_ID);
        assertEquals(VALID_GAME_ID, VALID_GAME.getGame_id());
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(null);
        GameShopException exception1 = assertThrows(GameShopException.class, () -> {
            specificGameService.getSpecificGamesByGameId(VALID_GAME_ID);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception1.getStatus());
        assertEquals("Game does not exist", exception1.getMessage());
        verify(specificGameRepository, times(0)).findAll();
        verify(gameRepository, times(1)).findById(VALID_GAME_ID);
    }

    @Test
    public void testGetSpecificGamesByGameId_InvalidGameId() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.getSpecificGamesByGameId(INVALID_GAME_ID_NEGATIVE);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game ID must be greater than 0", exception.getMessage());
        verify(specificGameRepository, never()).findAll();
    }

    // // --- Tests for getAllSpecificGames ---

    @Test
    public void testGetAllSpecificGames_Successful() {
        // Arrange
        SpecificGame specificGame1 = new SpecificGame(VALID_GAME);
        specificGame1.setSpecificGame_id(201);
        SpecificGame specificGame2 = new SpecificGame(VALID_GAME);
        specificGame2.setSpecificGame_id(202);

        List<SpecificGame> allSpecificGames = Arrays.asList(specificGame1, specificGame2);

        when(specificGameRepository.findAll()).thenReturn(allSpecificGames);

        // Act
        Iterable<SpecificGame> specificGames = specificGameService.getAllSpecificGames();

        // Assert
        List<SpecificGame> specificGameList = new ArrayList<>();
        specificGames.forEach(specificGameList::add);

        assertEquals(2, specificGameList.size());
        assertTrue(specificGameList.contains(specificGame1));
        assertTrue(specificGameList.contains(specificGame2));
        verify(specificGameRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllSpecificGames_Empty() {
        // Arrange
        when(specificGameRepository.findAll()).thenReturn(new ArrayList<>());

    GameShopException exception1 = assertThrows(GameShopException.class, () -> {
    specificGameService.getAllSpecificGames();
    });
    assertEquals(HttpStatus.NOT_FOUND, exception1.getStatus());
    assertEquals("No SpecificGame found at all", exception1.getMessage());
    verify(specificGameRepository, times(1)).findAll();

    }

    // // --- Tests for updateSpecificGameItemStatus ---

    @Test
    public void testUpdateSpecificGameItemStatus_Successful() {
        // Arrange
        SpecificGame specificGame = new SpecificGame(VALID_GAME);
        specificGame.setSpecificGame_id(VALID_SPECIFIC_GAME_ID);
        specificGame.setItemStatus(ItemStatus.Confirmed);

        when(specificGameRepository.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(specificGame);
        when(specificGameRepository.save(any(SpecificGame.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        SpecificGame updatedSpecificGame = specificGameService.updateSpecificGameItemStatus(VALID_SPECIFIC_GAME_ID, ItemStatus.Returned);

        // Assert
        assertNotNull(updatedSpecificGame);
        assertEquals(ItemStatus.Returned, updatedSpecificGame.getItemStatus());
        verify(specificGameRepository, times(1)).findById(VALID_SPECIFIC_GAME_ID);
        verify(specificGameRepository, times(1)).save(specificGame);
    }

    @Test
    public void testUpdateSpecificGameItemStatus_NullStatus() 
    {   VALID_SPECIFIC_GAME_ID=38;
        // Arrange
        SpecificGame specificGame = new SpecificGame(VALID_GAME);
        specificGame.setSpecificGame_id(VALID_SPECIFIC_GAME_ID);

        // when(specificGameRepository.findById(VALID_SPECIFIC_GAME_ID)).thenReturn(specificGame);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGameItemStatus(VALID_SPECIFIC_GAME_ID, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("ItemStatus cannot be null", exception.getMessage());
        verify(specificGameRepository, times(0)).findById(VALID_SPECIFIC_GAME_ID);
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }

    @Test
    public void testUpdateSpecificGameItemStatus_NonExistentSpecificGameId() {
        // Arrange
        when(specificGameRepository.findById(INVALID_SPECIFIC_GAME_ID)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGameItemStatus(INVALID_SPECIFIC_GAME_ID, ItemStatus.Returned);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("SpecificGame does not exist", exception.getMessage());
        verify(specificGameRepository, times(1)).findById(INVALID_SPECIFIC_GAME_ID);
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }

    @Test
    public void testUpdateSpecificGameItemStatus_InvalidSpecificGameId() {
        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            specificGameService.updateSpecificGameItemStatus(INVALID_SPECIFIC_GAME_ID_NEGATIVE, ItemStatus.Returned);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("SpecificGame ID must be greater than 0", exception.getMessage());
        verify(specificGameRepository, never()).findById(anyInt());
        verify(specificGameRepository, never()).save(any(SpecificGame.class));
    }
}
