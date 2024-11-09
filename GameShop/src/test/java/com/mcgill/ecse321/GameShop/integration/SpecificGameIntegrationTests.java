package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameListDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameRequestDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameResponseDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.SpecificGame.ItemStatus;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.SpecificGameRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpecificGameIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private SpecificGameRepository specificGameRepo;

    private int specificGameId;
    private int gameId;

    private static final String GAME_TITLE = "Test Gameaa";
    private static final String GAME_DESCRIPTION = "A game aused for testing.";
    private static final int GAME_PRICE = 50;
    private static final GameStatus GAME_STATUS = GameStatus.InStock;
    private static final int GAME_STOCK_QUANTITY = 100;
    private static final String GAME_PHOTO_URL = "http://exampaale.com/game.jpg";

    private static final ItemStatus SPECIFIC_GAME_ITEM_STATUS = ItemStatus.Confirmed;
    private static final ItemStatus UPDATED_SPECIFIC_GAME_ITEM_STATUS = ItemStatus.Returned;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        specificGameRepo.deleteAll();
        gameRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateValidSpecificGame() {
        // Ensure game exists
        GameRequestDto gameRequest = new GameRequestDto(GAME_TITLE, GAME_DESCRIPTION, GAME_PRICE, GAME_STATUS, GAME_STOCK_QUANTITY, GAME_PHOTO_URL);
        ResponseEntity<GameResponseDto> gameResponse = client.postForEntity("/games", gameRequest, GameResponseDto.class);

        // Assert Game Creation
        assertNotNull(gameResponse);
        assertEquals(HttpStatus.OK, gameResponse.getStatusCode());
        GameResponseDto gameRes = gameResponse.getBody();
        assertNotNull(gameRes);
        gameId = gameRes.getaGame_id();
        assertTrue(gameId > 0);

        // Arrange
        SpecificGameRequestDto request = new SpecificGameRequestDto(SPECIFIC_GAME_ITEM_STATUS, new ArrayList<>(), gameId);

        // Act
        ResponseEntity<SpecificGameResponseDto> response = client.postForEntity("/specificGames", request, SpecificGameResponseDto.class);

       // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificGameResponseDto specificGame = response.getBody();
        assertNotNull(specificGame);
        assertTrue(specificGame.getSpecificGame_id() > 0);
        specificGameId = specificGame.getSpecificGame_id();
        assertEquals(SPECIFIC_GAME_ITEM_STATUS, specificGame.getItemStatus());
        assertEquals(gameId, specificGame.getGame_id());
    }

    @Test
    @Order(2)
    public void testGetSpecificGameById() {
        // Arrange
        String url = String.format("/specificGames/%d", specificGameId);

        // Act
        ResponseEntity<SpecificGameResponseDto> response = client.getForEntity(url, SpecificGameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificGameResponseDto specificGame = response.getBody();
        assertNotNull(specificGame);
        assertEquals(specificGameId, specificGame.getSpecificGame_id());
        assertEquals(SPECIFIC_GAME_ITEM_STATUS, specificGame.getItemStatus());
        assertEquals(gameId, specificGame.getGame_id());
    }

    @Test
    @Order(3)
    public void testUpdateSpecificGameItemStatus() {
        // Arrange
        String url = String.format("/specificGames/%d/itemStatus", specificGameId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String newStatus = String.format("\"%s\"", UPDATED_SPECIFIC_GAME_ITEM_STATUS.toString()); // JSON expects a string value
        HttpEntity<String> requestEntity = new HttpEntity<>(newStatus, headers);

        // Act
        ResponseEntity<SpecificGameResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity, SpecificGameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificGameResponseDto updatedSpecificGame = response.getBody();
        assertNotNull(updatedSpecificGame);
        assertEquals(specificGameId, updatedSpecificGame.getSpecificGame_id());
        assertEquals(UPDATED_SPECIFIC_GAME_ITEM_STATUS, updatedSpecificGame.getItemStatus());
    }

    @Test
    @Order(4)
    public void testGetAllSpecificGames() {
        // Arrange
        // Create another SpecificGame
        SpecificGameRequestDto request = new SpecificGameRequestDto(SPECIFIC_GAME_ITEM_STATUS, new ArrayList<>(), gameId);
        ResponseEntity<SpecificGameResponseDto> createResponse = client.postForEntity("/specificGames", request, SpecificGameResponseDto.class);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        // Act
        ResponseEntity<SpecificGameListDto> response = client.getForEntity("/specificGames", SpecificGameListDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificGameListDto specificGames = response.getBody();
        assertNotNull(specificGames);
        List<SpecificGameSummaryDto> specificGameList = specificGames.getGames();
        assertNotNull(specificGameList);
        assertTrue(specificGameList.size() >= 2);
    }

    @Test
    @Order(5)
    public void testGetSpecificGamesByGameId() {
        // Arrange
        String url = String.format("/games/%d/specificGames", gameId);

        // Act
        ResponseEntity<SpecificGameListDto> response = client.getForEntity(url, SpecificGameListDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificGameListDto specificGames = response.getBody();
        assertNotNull(specificGames);
        List<SpecificGameSummaryDto> specificGameList = specificGames.getGames();
        assertNotNull(specificGameList);
        assertTrue(specificGameList.size() >= 2);

        // Verify that all specific games belong to the correct game
        for (SpecificGameSummaryDto sg : specificGameList) {
            assertEquals(gameId, sg.getGame().getGame_id());
        }
    }

    // @Test
    // @Order(6)
    // public void testDeleteSpecificGame() {
    //     // Arrange
    //     String url = String.format("/specificGames/%d", specificGameId);

    //     // Act
    //     ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);

    //     // Assert
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());

    //     // Verify Deletion
    //     ResponseEntity<SpecificGameResponseDto> getResponse = client.getForEntity(url, SpecificGameResponseDto.class);
    //     assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    // }
}
