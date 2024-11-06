// package com.mcgill.ecse321.GameShop.integration;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import java.util.List;

// import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryListDto;
// import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryRequestDto;
// import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryResponseDto;
// import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategorySummaryDto;
// import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
// import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
// import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
// import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
// import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformResponseDto;
// import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
// import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
// import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
// import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryRequestDto;
// import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryResponseDto;
// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
// import org.junit.jupiter.api.Order;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.junit.jupiter.api.TestMethodOrder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.http.*;

// import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
// import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;

// import com.mcgill.ecse321.GameShop.repository.AccountRepository;

// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @TestMethodOrder(OrderAnnotation.class)
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// public class CategoryIntegrationTests {

//     @Autowired
//     private TestRestTemplate client;

//     @Autowired
//     private CategoryRepository categoryRepo;

//     @Autowired
//     private ManagerRepository managerRepo;

//     @Autowired
//     private AccountRepository accountRepo;

//     private int categoryId;
//     private static final String MANAGER_EMAIL = "manager2@example.com";
//     private static final String MANAGER_PASSWORD = "managerPass";
//     private static final String MANAGER_USERNAME = "managerUser";
//     private static final String MANAGER_PHONE = "123-456-7890";
//     private static final String MANAGER_ADDRESS = "123 Manager Street";
//     private static final String CATEGORY_NAME = "Action Games";
//     private static final String UPDATED_CATEGORY_NAME = "Adventure Games";
//     private static final String SECOND_NAME = "Barbie Games";



//     @AfterAll
//     public void clearDatabase() {
//         categoryRepo.deleteAll();
//         managerRepo.deleteAll();
//         accountRepo.deleteAll();
//     }


//     @SuppressWarnings("null")
//     @Test
//     @Order(1)
//     public void testCreateValidCategory() {
//         // Ensure manager exists
//         AccountRequestDto manager = new AccountRequestDto(MANAGER_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE, MANAGER_ADDRESS);
//         ResponseEntity<AccountResponseDto> managerResponse = client.postForEntity("/account/manager", manager, AccountResponseDto.class);
//         // Arrange
//         CategoryRequestDto request = new CategoryRequestDto(CATEGORY_NAME, MANAGER_EMAIL);

//         // Act
//         ResponseEntity<CategoryResponseDto> response = client.postForEntity("/categories", request, CategoryResponseDto.class);

//         // Assert CREATED MANAGER
//         assertNotNull(managerResponse);
//         assertEquals(HttpStatus.OK, managerResponse.getStatusCode());
//         assertEquals(MANAGER_EMAIL, managerResponse.getBody().getEmail());
//         // ASSERT CREATED CATEGORY
        
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         CategoryResponseDto category = response.getBody();
//         assertNotNull(category);
//         assertTrue(category.getCategoryId() > 0);
//         this.categoryId = category.getCategoryId();
//         assertEquals(CATEGORY_NAME, category.getCategoryName());
//         assertEquals(MANAGER_EMAIL, category.getManagerEmail());
//     }

//     @Test
//     @Order(2)
//     public void testGetCategoryById() {
//         // Arrange
//         String url = String.format("/categories/%d", this.categoryId);
//         System.out.println(String.format("URL: %s", url));
//         // Act
//         ResponseEntity<CategoryResponseDto> response = client.getForEntity(url, CategoryResponseDto.class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         CategoryResponseDto category = response.getBody();
//         assertNotNull(category);
//         assertEquals(this.categoryId, category.getCategoryId());
//         assertEquals(CATEGORY_NAME, category.getCategoryName());
//         assertEquals(MANAGER_EMAIL, category.getManagerEmail());
//     }

//     @Test
//     @Order(3)
//     public void testUpdateCategory() {
//         // Arrange
//         String url = String.format("/categories/%d", this.categoryId);
//         System.out.println(String.format("URL: %s", url));
//         CategoryRequestDto updateRequest = new CategoryRequestDto(UPDATED_CATEGORY_NAME, MANAGER_EMAIL);
//         HttpEntity<CategoryRequestDto> requestEntity = new HttpEntity<>(updateRequest);

//         // Act
//         ResponseEntity<CategoryResponseDto> updateResponse = client.exchange(url, HttpMethod.PUT, requestEntity, CategoryResponseDto.class);

//         // Assert
//         assertNotNull(updateResponse);
//         assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
//         CategoryResponseDto updatedCategory = updateResponse.getBody();
//         assertNotNull(updatedCategory);
//         assertEquals(this.categoryId, updatedCategory.getCategoryId());
//         assertEquals(UPDATED_CATEGORY_NAME, updatedCategory.getCategoryName());
//         assertEquals(MANAGER_EMAIL, updatedCategory.getManagerEmail());
//     }

//     @Test
//     @Order(4)
//     public void testGetAllCategories() {
//         // Arrange
//         // Create another category
//         CategoryRequestDto request = new CategoryRequestDto(SECOND_NAME, MANAGER_EMAIL);
//         ResponseEntity<CategoryResponseDto> createResponse = client.postForEntity("/categories", request, CategoryResponseDto.class);
//         assertNotNull(createResponse);
//         assertEquals(HttpStatus.OK, createResponse.getStatusCode());
//         // Act
//         ResponseEntity<CategoryListDto> response = client.getForEntity("/categories", CategoryListDto.class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         CategoryListDto categories = response.getBody();
//         assertNotNull(categories);
//         List<CategorySummaryDto> categoryList = categories.getCategories();
//         assertNotNull(categoryList);
//         assertTrue(categoryList.size() >= 2);}
    

//     @Test
//     @Order(5)
//     public void testDeleteCategory() {
//         // Arrange
//         String url = String.format("/categories/%d", this.categoryId);
//         ResponseEntity<PlatformResponseDto> responseBefore = client.getForEntity(url, PlatformResponseDto.class);

//         // Assert
//         assertNotNull(responseBefore);
//         assertEquals(HttpStatus.OK, responseBefore.getStatusCode(), "Category does not exist before deletion");
    
//         // Act
//         ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);
    
//         // Log for debugging
//         System.out.println("Response status code for DELETE: " + response.getStatusCode());
    
//         // Assert
//         assertNotNull(response);
    
//         // Verify Deletion by trying to fetch the category again
//         ResponseEntity<CategoryResponseDto> getResponse = client.getForEntity(url, CategoryResponseDto.class);
//         assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
//     }

//     @Test
//     @Order(6)
//     public void testFindAllGamesInCategory() {
//         // Arrange
//         String url = String.format("/categories/%d/games", this.categoryId);
//         // Create a game in the category
//         GameRequestDto gameRequest = new GameRequestDto( "rambo 2", "this game is awesome", 59,GameStatus.InStock,5, "www.game.com");
//         gameRequest.setCategories(List.of(this.categoryId));
//         ResponseEntity<GameResponseDto> createResponse = client.postForEntity("/games", gameRequest, GameResponseDto.class);
        
//         assertNotNull(createResponse);
//         assertEquals(HttpStatus.OK, createResponse.getStatusCode(), "Game not created");

//         // Act
//         ResponseEntity<GameListDto> response = client.getForEntity(url, GameListDto.class);

//         // Assert
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode(), "Games not found");
//         GameListDto games = response.getBody();
//         assertNotNull(games);
//         List<GameSummaryDto> gameList = games.getGames();
//         assertNotNull(gameList);
//         // assertTrue(gameList.stream().anyMatch(game -> "New Game".equals(game.getGameName())));
//     }
    

// }

