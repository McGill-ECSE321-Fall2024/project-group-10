
package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;


import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformListDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformRequestDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformResponseDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformSummaryDto;

import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Platform;

import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;

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

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;

import com.mcgill.ecse321.GameShop.repository.AccountRepository;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlatformIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PlatformRepository platformRepo;

    @Autowired
    private ManagerRepository managerRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private GameRepository gameRepo;

    private int platformId;
    private static final String MANAGER_EMAIL = "managerofPlatform@example.com";
    private static final String MANAGER_PASSWORD = "managerPass";
    private static final String MANAGER_USERNAME = "managerUser";
    private static final String MANAGER_PHONE = "123-456-7890";
    private static final String MANAGER_ADDRESS = "123 Manager Street";
    private static final String PLATFORM_NAME = "PlayStation 5";
    private static final String UPDATED_PLATFORM_NAME = "Xbox Series X";
    private static final String SECOND_NAME = "Nintendo Switch";


    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        for (Game game : gameRepo.findAll()) {
            List<Platform> platforms = new ArrayList<>(game.getPlatforms());
            for (Platform platform : platforms) {
                game.removePlatform(platform);
            }
            gameRepo.save(game);
        }

        gameRepo.deleteAll();
        platformRepo.deleteAll();
      
        managerRepo.deleteAll();
        accountRepo.deleteAll();
    }
    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateValidPlatform() {
        AccountRequestDto manager = new AccountRequestDto(MANAGER_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE, MANAGER_ADDRESS);
        ResponseEntity<AccountResponseDto> managerResponse = client.postForEntity("/account/manager", manager, AccountResponseDto.class);
        PlatformRequestDto request = new PlatformRequestDto(PLATFORM_NAME, MANAGER_EMAIL);

        ResponseEntity<PlatformResponseDto> response = client.postForEntity("/platforms", request, PlatformResponseDto.class);

        assertNotNull(managerResponse);
        assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
        assertEquals(MANAGER_EMAIL, managerResponse.getBody().getEmail());
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlatformResponseDto platform = response.getBody();
        assertNotNull(platform);
        assertTrue(platform.getPlatformId() > 0);
        this.platformId = platform.getPlatformId();
        assertEquals(PLATFORM_NAME, platform.getPlatformName());
        assertEquals(MANAGER_EMAIL, platform.getManagerEmail());
    }

    @Test
    @Order(2)
    public void testGetPlatformById() {
        String url = String.format("/platforms/%d", this.platformId);
        ResponseEntity<PlatformResponseDto> response = client.getForEntity(url, PlatformResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlatformResponseDto platform = response.getBody();
        assertNotNull(platform);
        assertEquals(this.platformId, platform.getPlatformId());
        assertEquals(PLATFORM_NAME, platform.getPlatformName());
        assertEquals(MANAGER_EMAIL, platform.getManagerEmail());
    }

    @Test
    @Order(3)
    public void testUpdatePlatform() {
        String url = String.format("/platforms/%d", this.platformId);
        PlatformRequestDto updateRequest = new PlatformRequestDto(UPDATED_PLATFORM_NAME, MANAGER_EMAIL);
        HttpEntity<PlatformRequestDto> requestEntity = new HttpEntity<>(updateRequest);

        ResponseEntity<PlatformResponseDto> updateResponse = client.exchange(url, HttpMethod.PUT, requestEntity, PlatformResponseDto.class);

        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        PlatformResponseDto updatedPlatform = updateResponse.getBody();
        assertNotNull(updatedPlatform);
        assertEquals(this.platformId, updatedPlatform.getPlatformId());
        assertEquals(UPDATED_PLATFORM_NAME, updatedPlatform.getPlatformName());
        assertEquals(MANAGER_EMAIL, updatedPlatform.getManagerEmail());
    }

      @Test
    @Order(4)
    public void testGetAllPlatforms() {
        PlatformRequestDto request = new PlatformRequestDto(SECOND_NAME, MANAGER_EMAIL);
        ResponseEntity<PlatformResponseDto> createResponse = client.postForEntity("/platforms", request, PlatformResponseDto.class);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        
        ResponseEntity<PlatformListDto> response = client.getForEntity("/platforms", PlatformListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PlatformListDto platforms = response.getBody();
        assertNotNull(platforms);
        List<PlatformSummaryDto> platformList = platforms.getPlatforms();
        assertNotNull(platformList);
        assertTrue(platformList.size() >= 2);
        
    }
    @Test
    @Order(5)
    public void testDeletePlatform() {
        String url = String.format("/platforms/%d", this.platformId);
        ResponseEntity<PlatformResponseDto> responseBefore = client.getForEntity(url, PlatformResponseDto.class);

        assertNotNull(responseBefore);
        assertEquals(HttpStatus.OK, responseBefore.getStatusCode(), "Platform does not exist before deletion");
    
        ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);
    
        assertNotNull(response);
    
        ResponseEntity<PlatformResponseDto> getResponse = client.getForEntity(url, PlatformResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @SuppressWarnings("null")
    @Test
@Order(6)
public void testFindAllGamesInPlatform() {
    // Arrange: Create target platform and another platform
    PlatformRequestDto platformRequest = new PlatformRequestDto("Nintendo Switch", MANAGER_EMAIL);
    ResponseEntity<PlatformResponseDto> platformResponse = client.postForEntity("/platforms", platformRequest, PlatformResponseDto.class);
    assertEquals(HttpStatus.OK, platformResponse.getStatusCode(), "Platform creation failed");
    PlatformResponseDto initialPlatform = platformResponse.getBody();
    assertNotNull(initialPlatform);
    assertNotNull(platformResponse.getBody().getPlatformId());
    int targetPlatformId = platformResponse.getBody().getPlatformId();

    PlatformRequestDto otherPlatformRequest = new PlatformRequestDto("PC", MANAGER_EMAIL);
    ResponseEntity<PlatformResponseDto> otherPlatformResponse = client.postForEntity("/platforms", otherPlatformRequest, PlatformResponseDto.class);
    assertEquals(HttpStatus.OK, otherPlatformResponse.getStatusCode(), "Other platform creation failed");
    PlatformResponseDto otherPlatform = otherPlatformResponse.getBody();
    assertNotNull(otherPlatform);
    assertNotNull(otherPlatformResponse.getBody().getPlatformId());
    int otherPlatformId = otherPlatformResponse.getBody().getPlatformId();

    // Create games associated with platforms
    GameRequestDto gameRequest1 = new GameRequestDto("Zelda", "Adventure game", 60, GameStatus.InStock, 3, "zelda.com");
    gameRequest1.setPlatforms(List.of(targetPlatformId, otherPlatformId));
    ResponseEntity<GameResponseDto> gameResponse1 = client.postForEntity("/games", gameRequest1, GameResponseDto.class);
    assertEquals(HttpStatus.OK, gameResponse1.getStatusCode(), "Failed to create Game 1");
    int gameId1 = gameResponse1.getBody().getaGame_id();

    GameRequestDto gameRequest2 = new GameRequestDto("Mario", "Platform game", 50, GameStatus.InStock, 2, "mario.com");
    gameRequest2.setPlatforms(List.of(targetPlatformId));
    ResponseEntity<GameResponseDto> gameResponse2 = client.postForEntity("/games", gameRequest2, GameResponseDto.class);
    assertEquals(HttpStatus.OK, gameResponse2.getStatusCode(), "Failed to create Game ");
    int gameId2 = gameResponse2.getBody().getaGame_id();

    GameRequestDto gameRequest3 = new GameRequestDto("Halo", "Shooter game", 70, GameStatus.InStock, 4, "halo.com");
    gameRequest3.setPlatforms(List.of(otherPlatformId));
    ResponseEntity<GameResponseDto> gameResponse3 = client.postForEntity("/games", gameRequest3, GameResponseDto.class);
    assertEquals(HttpStatus.OK, gameResponse3.getStatusCode(), "Failed to create Game 3");
    int gameId3 = gameResponse3.getBody().getaGame_id();

    // Act: Fetch all games within the target platform
    String url = String.format("/platforms/%d/games", targetPlatformId);
    ResponseEntity<GameListDto> response = client.getForEntity(url, GameListDto.class);

    // Assert: Verify that the response contains only the games associated with the target platform
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Games not found in platform");
    GameListDto games = response.getBody();
    assertNotNull(games);
    List<GameSummaryDto> gameList = games.getGames();
    assertNotNull(gameList);

    // Verify that exactly 2 games are returned in the target platform and they match the expected game IDs
    assertEquals(2, gameList.size(), "Expected 2 games in the platform");

    List<Integer> returnedGameIds = new ArrayList<>();
    for (GameSummaryDto game : gameList) {
        returnedGameIds.add(game.getGameId());
    }
    assertTrue(returnedGameIds.contains(gameId1), "Expected to find game with ID: " + gameId1);
    assertTrue(returnedGameIds.contains(gameId2), "Expected to find game with ID: " + gameId2);
    assertFalse(returnedGameIds.contains(gameId3), "Did not expect to find game with ID: " + gameId3);
}
















}