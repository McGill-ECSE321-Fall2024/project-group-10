package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.http.HttpMethod;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryRequestDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryResponseDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategorySummaryDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformRequestDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformResponseDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformSummaryDto;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GameIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlatformRepository platformRepository;

    private static final String GAME_TITLE = "Game Title";
    private static final String GAME_DESCRIPTION = "Game Description";
    private static final int GAME_PRICE = 50;
    private static final GameStatus GAME_STATUS = GameStatus.InStock;
    private static final int GAME_STOCK_QUANTITY = 10;
    private static final String GAME_PHOTO_URL = "https://www.exampleayre.com/game.jpg";

    private static final String MANAGER_EMAIL = "managerovjchkdhjkgfsjkghdsfaghjkfPlatformmm@example.com";
    private static final String MANAGER_PASSWORD = "managerPass";
    private static final String MANAGER_USERNAME = "managerUser";
    private static final String MANAGER_PHONE = "123-456-7890";
    private static final String MANAGER_ADDRESS = "123 Manager Street";

    private int game_id;
    private List<Integer> gameIds = new ArrayList<>();
    // private int platformId;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {

        for (Category category : categoryRepository.findAll()) {
            category.removeManager();
            categoryRepository.save(category);
        }

        for (Platform platform : platformRepository.findAll()) {
            platform.removeManager();
            platformRepository.save(platform);
        }

        for (Game game : gameRepository.findAll()) {
            Iterator<Category> categoryIterator = game.getCategories().iterator();
            while (categoryIterator.hasNext()) {
                Category category = categoryIterator.next();
            }
    
            Iterator<Platform> platformIterator = game.getPlatforms().iterator();
            while (platformIterator.hasNext()) {
                Platform platform = platformIterator.next();
            }
            gameRepository.save(game);
        }

        managerRepository.deleteAll();
        gameRepository.deleteAll();
        categoryRepository.deleteAll();
        platformRepository.deleteAll();
        accountRepo.deleteAll();
    }

    @SuppressWarnings("null") // TODO check this later
    @Test
    @Order(1)
    public void testCreateValidGame() {
        ArrayList<Integer> platformIds = new ArrayList<>();
        ArrayList<Integer> categoryIds = new ArrayList<>();

        AccountRequestDto manager = new AccountRequestDto(MANAGER_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE, MANAGER_ADDRESS);
        ResponseEntity<AccountResponseDto> managerResponse = client.postForEntity("/account/manager", manager, AccountResponseDto.class);
        assertNotNull(managerResponse);
        assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
        assertEquals(MANAGER_EMAIL, managerResponse.getBody().getEmail());

        for (int i = 1; i <= 3; i++) {
            PlatformRequestDto platformRequestDto = new PlatformRequestDto("Platform Name", MANAGER_EMAIL);
            ResponseEntity<PlatformResponseDto> platformResponse = client.postForEntity("/platforms", platformRequestDto, PlatformResponseDto.class);
            assertNotNull(platformResponse);
            assertEquals(HttpStatus.OK, platformResponse.getStatusCode());
            assertEquals("Platform Name", platformResponse.getBody().getPlatformName());
            platformIds.add(platformResponse.getBody().getPlatformId());

            CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Category Name", MANAGER_EMAIL);
            ResponseEntity<CategoryResponseDto> categoryResponse = client.postForEntity("/categories", categoryRequestDto, CategoryResponseDto.class);
            assertNotNull(categoryResponse);
            assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
            assertEquals("Category Name", categoryResponse.getBody().getCategoryName());
            categoryIds.add(categoryResponse.getBody().getCategoryId());
        }

        GameRequestDto gameRequestDto = new GameRequestDto(GAME_TITLE, GAME_DESCRIPTION, GAME_PRICE, GAME_STATUS, GAME_STOCK_QUANTITY, GAME_PHOTO_URL);
        gameRequestDto.setCategories(categoryIds);
        gameRequestDto.setPlatforms(platformIds);

        // Act
        ResponseEntity<GameResponseDto> response = client.postForEntity("/games", gameRequestDto, GameResponseDto.class);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getaGame_id() > 0, "The ID should be positive");
        GameResponseDto game = response.getBody();
        assertNotNull(game);
        this.game_id = game.getaGame_id();
        this.gameIds.add(game.getaGame_id());
        assertEquals(GAME_TITLE, response.getBody().getaTitle());
        assertEquals(GAME_DESCRIPTION, response.getBody().getaDescription());
        assertEquals(GAME_PRICE, response.getBody().getaPrice());
        assertEquals(GAME_STATUS, response.getBody().getaGameStatus());
        assertEquals(GAME_STOCK_QUANTITY, response.getBody().getaStockQuantity());
        assertEquals(GAME_PHOTO_URL, response.getBody().getaPhotoUrl()); // Check why changing the name fixed it.

        for (CategorySummaryDto category : response.getBody().getCategories().getCategories()) {
            assertTrue(categoryIds.contains(category.getCategoryId()));
        }

        for (PlatformSummaryDto platformInResponse : response.getBody().getPlatforms().getPlatforms()) {
            assertTrue(platformIds.contains(platformInResponse.getPlatformId()));
        }
    }

    @Test
    @Order(2)
    public void testGetGameById() {
        String url = "/games/" + this.game_id;
        ResponseEntity<GameResponseDto> response = client.getForEntity(url, GameResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GameResponseDto game = response.getBody();
        assertNotNull(game);
        assertEquals(this.game_id, game.getaGame_id());
        assertEquals(GAME_TITLE, game.getaTitle());
        assertEquals(GAME_DESCRIPTION, game.getaDescription());
        assertEquals(GAME_PRICE, game.getaPrice());
        assertEquals(GAME_STATUS, game.getaGameStatus());
        assertEquals(GAME_STOCK_QUANTITY, game.getaStockQuantity());
        assertEquals(GAME_PHOTO_URL, game.getaPhotoUrl());
    }

    @Test
    @Order(3)
    public void testGetAllGames() {

        ArrayList<Integer> platformIds = new ArrayList<>();
        ArrayList<Integer> categoryIds = new ArrayList<>();

        AccountRequestDto manager = new AccountRequestDto(MANAGER_EMAIL + "m", MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE, MANAGER_ADDRESS);
        ResponseEntity<AccountResponseDto> managerResponse = client.postForEntity("/account/manager", manager, AccountResponseDto.class);
        assertNotNull(managerResponse);
        assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
        assertEquals(MANAGER_EMAIL + "m", managerResponse.getBody().getEmail());

        for (int i = 1; i <= 3; i++) {
            PlatformRequestDto platformRequestDto = new PlatformRequestDto("Platform Name", MANAGER_EMAIL + "m");
            ResponseEntity<PlatformResponseDto> platformResponse = client.postForEntity("/platforms", platformRequestDto, PlatformResponseDto.class);
            assertNotNull(platformResponse);
            assertEquals(HttpStatus.OK, platformResponse.getStatusCode());
            assertEquals("Platform Name", platformResponse.getBody().getPlatformName());
            platformIds.add(platformResponse.getBody().getPlatformId());

            CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Category Name", MANAGER_EMAIL + "m");
            ResponseEntity<CategoryResponseDto> categoryResponse = client.postForEntity("/categories", categoryRequestDto, CategoryResponseDto.class);
            assertNotNull(categoryResponse);
            assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
            assertEquals("Category Name", categoryResponse.getBody().getCategoryName());
            categoryIds.add(categoryResponse.getBody().getCategoryId());
        }

        GameRequestDto gameRequestDto = new GameRequestDto(GAME_TITLE + "V2.0", GAME_DESCRIPTION, GAME_PRICE, GAME_STATUS, GAME_STOCK_QUANTITY, GAME_PHOTO_URL);
        gameRequestDto.setCategories(categoryIds);
        gameRequestDto.setPlatforms(platformIds);

        ResponseEntity<GameResponseDto> response = client.postForEntity("/games", gameRequestDto, GameResponseDto.class);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getaGame_id() > 0, "The ID should be positive");
        this.gameIds.add(response.getBody().getaGame_id());
        ResponseEntity<GameListDto> allGamesResponse = client.getForEntity("/games", GameListDto.class);

        GameListDto games = allGamesResponse.getBody();
        assertNotNull(games);
        assertTrue(games.getNumberOfGames() == 2, "There should be two games");

        List<GameSummaryDto> gamesList = games.getGames();
        assertEquals(HttpStatus.OK, allGamesResponse.getStatusCode());
        assertTrue(gamesList.get(1).getGameId() > 0, "The ID should be positive");
        assertTrue(gamesList.get(1).getGameId() > 0, "The ID should be positive");

        for (GameSummaryDto game : gamesList) {
            assertTrue(gameIds.contains(game.getGameId()));
        }
    }
    

    // @Test
    // @Order(4)
    // public void testUpdateGame() {
    //     String newTitle = "New Title";
    //     String newDescription = "New Description";
    //     int newPrice = 100;
    //     GameStatus newStatus = GameStatus.OutOfStock;
    //     int newStockQuantity = 5;
    //     String newPhotoUrl = "https://www.example.com/newgame.jpg";

    //     ArrayList<Integer> platformIds = new ArrayList<>();
    //     ArrayList<Integer> categoryIds = new ArrayList<>();

    //     for (int i = 1; i <= 3; i++) {
    //         PlatformRequestDto platformRequestDto = new PlatformRequestDto("Platform Name", MANAGER_EMAIL);
    //         ResponseEntity<PlatformResponseDto> platformResponse = client.postForEntity("/platforms", platformRequestDto, PlatformResponseDto.class);
    //         assertNotNull(platformResponse);
    //         assertEquals(HttpStatus.OK, platformResponse.getStatusCode());
    //         assertEquals("Platform Name", platformResponse.getBody().getPlatformName());
    //         platformIds.add(platformResponse.getBody().getPlatformId());

    //         CategoryRequestDto categoryRequestDto = new CategoryRequestDto("Category Name", MANAGER_EMAIL);
    //         ResponseEntity<CategoryResponseDto> categoryResponse = client.postForEntity("/categories", categoryRequestDto, CategoryResponseDto.class);
    //         assertNotNull(categoryResponse);
    //         assertEquals(HttpStatus.OK, categoryResponse.getStatusCode());
    //         assertEquals("Category Name", categoryResponse.getBody().getCategoryName());
    //         categoryIds.add(categoryResponse.getBody().getCategoryId());
    //     }
    //     HttpEntity<GameRequestDto> gameRequestDto = new HttpEntity<>(new GameRequestDto(newTitle, newDescription, newPrice, newStatus, newStockQuantity, newPhotoUrl));

    //     ResponseEntity<GameResponseDto> updateResponse = client.exchange("/games/" + this.game_id, HttpMethod.PUT, gameRequestDto, GameResponseDto.class);

    //     GameResponseDto updatedGame = updateResponse.getBody();

    //     assertNotNull(updatedGame);
    //     assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    //     assertEquals(this.game_id, updatedGame.getaGame_id());
    //     assertEquals(newTitle, updatedGame.getaTitle());
    //     assertEquals(newDescription, updatedGame.getaDescription());
    //     assertEquals(newPrice, updatedGame.getaPrice());
    //     assertEquals(newStatus, updatedGame.getaGameStatus());
    //     assertEquals(newStockQuantity, updatedGame.getaStockQuantity());
    //     assertEquals(newPhotoUrl, updatedGame.getaPhotoUrl());
    // }

}