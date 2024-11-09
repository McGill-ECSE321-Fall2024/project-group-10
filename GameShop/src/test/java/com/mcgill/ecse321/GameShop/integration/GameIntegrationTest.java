package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryRequestDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryResponseDto;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategorySummaryDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class GameIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private CategoryRepository categoryReposiotry;

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
    // private int platformId;

    @BeforeAll
    @AfterAll
    public void clearDatabase() {

        for (Category category : categoryReposiotry.findAll()) {
            category.removeManager();
            categoryReposiotry.save(category);
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
        categoryReposiotry.deleteAll();
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
        this.game_id = response.getBody().getaGame_id();
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
    
}