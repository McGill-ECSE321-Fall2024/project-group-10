package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.WishListDto.WishListRequestDto;
import com.mcgill.ecse321.GameShop.dto.WishListDto.WishListResponseDto;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.WishList;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.http.*;

import java.util.List;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WishListIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private WishListRepository wishListRepo;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private AccountRepository accountRepo;

    private int wishListId;
    private int gameId1;
    private int gameId2;

    private static final String CUSTOMER_EMAIL = "customerofWishlist@example.com";
    private static final String CUSTOMER_USERNAME = "customerUser";
    private static final String CUSTOMER_PASSWORD = "customerPass";
    private static final String CUSTOMER_PHONE = "123-456-7890";
    private static final String CUSTOMER_ADDRESS = "123 Customer Street";
    private static final String GAME_TITLE_1 = "Game One";
    private static final String GAME_TITLE_2 = "Game Two";

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        wishListRepo.deleteAll();
        gameRepo.deleteAll();
        accountRepo.deleteAll();
    }

    @Test
    @Order(1)
    public void testGetWishListForCustomer() {
        // Create customer account
        AccountRequestDto customerRequest = new AccountRequestDto(
                CUSTOMER_EMAIL, CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE, CUSTOMER_ADDRESS);
        ResponseEntity<AccountResponseDto> customerResponse = client.postForEntity(
                "/account/customer", customerRequest, AccountResponseDto.class);
        assertNotNull(customerResponse);
        AccountResponseDto customer = customerResponse.getBody();
        assertNotNull(customer);
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode(), "Failed to create customer account");
        assertEquals(CUSTOMER_EMAIL, customer.getEmail(), "Customer email mismatch");
        // Create wishlist
        String url = String.format("/account/customer/%s/wishlist", CUSTOMER_EMAIL);
        ResponseEntity<WishListResponseDto> response = client.getForEntity(url, WishListResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get wishlist for customer");
        WishListResponseDto wishList = response.getBody();
        assertNotNull(wishList, "Response body is null");
        this.wishListId = wishList.getWishList_id();
        assertEquals(CUSTOMER_EMAIL, wishList.getCustomerEmail(), "Customer email mismatch");
    }

    // @Test
    // @Order(2)
    // public void testGetWishListById() {
    //     String url = String.format("/wishlist/%d", this.wishListId);
    //     ResponseEntity<WishListResponseDto> response = client.getForEntity(url, WishListResponseDto.class);
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get wishlist by ID");
    //     WishListResponseDto wishList = response.getBody();
    //     assertNotNull(wishList, "Response body is null");
    //     assertEquals(this.wishListId, wishList.getWishList_id(), "Wishlist ID mismatch");
    //     assertEquals(CUSTOMER_EMAIL, wishList.getCustomerEmail(), "Customer email mismatch");
    // }









}


    // }
//     @Test
//     @Order(2)
//     public void testGetWishListById(){
//         String url = String.format("/wishlist/%d", this.wishListId);
//         ResponseEntity<WishListResponseDto> response = client.getForEntity(url, WishListResponseDto.class);
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get wishlist by ID");
//         WishListResponseDto wishList = response.getBody();
//         assertNotNull(wishList, "Response body is null");
//         assertEquals(this.wishListId, wishList.getWishList_id(), "Wishlist ID mismatch");
//         assertEquals(WISHLIST_TITLE, wishList.getTitle(), "Wishlist title mismatch");
//         assertEquals(CUSTOMER_EMAIL, wishList.getCustomerEmail(), "Customer email mismatch");
//     }
//     @Test
//     @Order(3)
//     public void testUpdateWishList(){
//         String newTitle = "My New Favorite Games";
//         String url = String.format("/wishlist/%d", this.wishListId);
//         WishListRequestDto wishListRequest = new WishListRequestDto(newTitle, CUSTOMER_EMAIL);
//         HttpEntity<WishListRequestDto> request = new HttpEntity<>(wishListRequest);
//         ResponseEntity<WishListResponseDto> response = client.exchange(
//                 url, HttpMethod.PUT, request, WishListResponseDto.class);
        
//         assertNotNull(response);
//         assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to update wishlist");
//         WishListResponseDto wishList = response.getBody();
//         assertNotNull(wishList, "Response body is null");
//         assertEquals(this.wishListId, wishList.getWishList_id(), "Wishlist ID mismatch");
//         assertEquals(newTitle, wishList.getTitle(), "Wishlist title mismatch");
//         assertEquals(CUSTOMER_EMAIL, wishList.getCustomerEmail(), "Customer email mismatch");
//     }

//     // @Test
//     // @Order(2)
//     // public void testAddGameToWishlist() {
//     //     // Create games
//     //     GameRequestDto gameRequest1 = new GameRequestDto(
//     //             GAME_TITLE_1, "First game description", 59.99, GameStatus.InStock, 10, "game1.jpg");
//     //     ResponseEntity<GameResponseDto> gameResponse1 = client.postForEntity(
//     //             "/games", gameRequest1, GameResponseDto.class);

//     //     assertEquals(HttpStatus.OK, gameResponse1.getStatusCode(), "Failed to create game 1");
//     //     assertNotNull(gameResponse1.getBody(), "Game 1 response body is null");
//     //     gameId1 = gameResponse1.getBody().getaGame_id();

//     //     GameRequestDto gameRequest2 = new GameRequestDto(
//     //             GAME_TITLE_2, "Second game description", 49.99, GameStatus.InStock, 5, "game2.jpg");
//     //     ResponseEntity<GameResponseDto> gameResponse2 = client.postForEntity(
//     //             "/games", gameRequest2, GameResponseDto.class);

//     //     assertEquals(HttpStatus.OK, gameResponse2.getStatusCode(), "Failed to create game 2");
//     //     assertNotNull(gameResponse2.getBody(), "Game 2 response body is null");
//     //     gameId2 = gameResponse2.getBody().getaGame_id();

//     //     // Add games to wishlist
//     //     String url = String.format("/wishlist/%d/%d", wishListId, gameId1);
//     //     ResponseEntity<WishListResponseDto> addGameResponse = client.exchange(
//     //             url, HttpMethod.PUT, null, WishListResponseDto.class);

//     //     assertEquals(HttpStatus.OK, addGameResponse.getStatusCode(), "Failed to add game 1 to wishlist");

//     //     url = String.format("/wishlist/%d/%d", wishListId, gameId2);
//     //     addGameResponse = client.exchange(
//     //             url, HttpMethod.PUT, null, WishListResponseDto.class);

//     //     assertEquals(HttpStatus.OK, addGameResponse.getStatusCode(), "Failed to add game 2 to wishlist");
//     // }

//     // @Test
//     // @Order(3)
//     // public void testGetAllGamesInWishlist() {
//     //     String url = String.format("/wishlist/%d/", wishListId);
//     //     ResponseEntity<GameListDto> response = client.getForEntity(url, GameListDto.class);

//     //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get games in wishlist");
//     //     assertNotNull(response.getBody(), "Response body is null");

//     //     List<GameSummaryDto> games = response.getBody().getGames();
//     //     assertNotNull(games, "Games list is null");
//     //     assertEquals(2, games.size(), "Expected 2 games in wishlist");

//     //     // Verify that the games are the ones we added
//     //     assertTrue(games.stream().anyMatch(game -> game.getGameId() == gameId1), "Game 1 not found in wishlist");
//     //     assertTrue(games.stream().anyMatch(game -> game.getGameId() == gameId2), "Game 2 not found in wishlist");
//     // }

//     // @Test
//     // @Order(4)
//     // public void testGetGameInWishlist() {
//     //     String url = String.format("/wishlist/%d/%d", wishListId, gameId1);
//     //     ResponseEntity<GameResponseDto> response = client.getForEntity(url, GameResponseDto.class);

//     //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to get game from wishlist");
//     //     assertNotNull(response.getBody(), "Response body is null");
//     //     assertEquals(gameId1, response.getBody().getaGame_id(), "Game ID mismatch");
//     // }

//     // @Test
//     // @Order(5)
//     // public void testRemoveGameFromWishlist() {
//     //     String url = String.format("/wishlist/%d/%d", wishListId, gameId1);
//     //     ResponseEntity<WishListResponseDto> response = client.exchange(
//     //             url, HttpMethod.DELETE, null, WishListResponseDto.class);

//     //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to remove game from wishlist");

//     //     // Verify that the game is removed
//     //     String getUrl = String.format("/wishlist/%d/", wishListId);
//     //     ResponseEntity<GameListDto> getResponse = client.getForEntity(getUrl, GameListDto.class);

//     //     assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Failed to get games in wishlist after removal");
//     //     List<GameSummaryDto> games = getResponse.getBody().getGames();

//     //     assertEquals(1, games.size(), "Expected 1 game in wishlist after removal");
//     //     assertFalse(games.stream().anyMatch(game -> game.getGameId() == gameId1), "Game 1 still present in wishlist");
//     // }

//     // @Test
//     // @Order(6)
//     // public void testRemoveAllGamesFromWishlist() {
//     //     String url = String.format("/wishlist/%d", wishListId);
//     //     ResponseEntity<WishListResponseDto> response = client.exchange(
//     //             url, HttpMethod.PUT, null, WishListResponseDto.class);

//     //     assertEquals(HttpStatus.OK, response.getStatusCode(), "Failed to remove all games from wishlist");

//     //     // Verify that the wishlist is empty
//     //     String getUrl = String.format("/wishlist/%d/", wishListId);
//     //     ResponseEntity<GameListDto> getResponse = client.getForEntity(getUrl, GameListDto.class);

//     //     assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Failed to get games in wishlist after clearing");
//     //     List<GameSummaryDto> games = getResponse.getBody().getGames();

//     //     assertEquals(0, games.size(), "Expected 0 games in wishlist after clearing");
//     // }
// }
