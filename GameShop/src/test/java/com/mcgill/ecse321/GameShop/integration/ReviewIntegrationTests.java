package com.mcgill.ecse321.GameShop.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
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
import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyRequestDto;
import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyResponseDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewListDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewRequestDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewResponseDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewSummaryDto;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Reply.ReviewRating;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Review.GameRating;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;
import com.mcgill.ecse321.GameShop.repository.ReviewRepository;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewIntegrationTests {
    
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryRepository categoryReposiotry;

    @Autowired
    private PlatformRepository platformRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WishListRepository wishListRepository;

    private static final String REVIEW_DESCRIPTION = "This is a review of game 1";
    private static final Review.GameRating REVIEW_GAME_RATING = Review.GameRating.Four;
    
    private static final String GAME_TITLE = "Game Title1";
    private static final String GAME_DESCRIPTION = "Game Description1";
    private static final int GAME_PRICE = 51;
    private static final GameStatus GAME_STATUS = GameStatus.InStock;
    private static final int GAME_STOCK_QUANTITY = 11;
    private static final String GAME_PHOTO_URL = "https://www.exampleger.com/game.jpg";

    private static final String MANAGER_EMAIL = "hellothisismanagermail@gigi.com";
    private static final String MANAGER_PASSWORD = "managerpasswordgertest";
    private static final String MANAGER_USERNAME = "hellothisismanagerusername";
    private static final String MANAGER_PHONE = "123-456-7899";
    private static final String MANAGER_ADDRESS = "12321 Manager Street";

    private static final String CUSTOMER_EMAIL = "hellotom@mail.com";
    private static final String CUSTOMER_PASSWORD = "customerpassword";
    private static final String CUSTOMER_USERNAME = "hellotom";
    private static final String CUSTOMER_PHONE = "123-456-7891";
    private static final String CUSTOMER_ADDRESS = "12321 Customer Street";

    private int review_id;
    private int game_id;
    
    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        
        for (Review review : reviewRepository.findAll()) {
            reviewRepository.delete(review);
        }
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
        wishListRepository.deleteAll();
        managerRepository.deleteAll();
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        categoryReposiotry.deleteAll();
        platformRepository.deleteAll();
        accountRepository.deleteAll(); 
    }

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateValidReview(){
        ArrayList<Integer> platformIds = new ArrayList<>();
        ArrayList<Integer> categoryIds = new ArrayList<>();
        AccountRequestDto customer = new AccountRequestDto(CUSTOMER_EMAIL, CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE, CUSTOMER_ADDRESS);
        ResponseEntity<AccountResponseDto> customerResponse = client.postForEntity("/account/customer", customer, AccountResponseDto.class);
        assertNotNull(customerResponse);
        assertEquals(HttpStatus.OK, customerResponse.getStatusCode());
        assertEquals(CUSTOMER_EMAIL, customerResponse.getBody().getEmail());
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
        LocalDate date = LocalDate.now();
        ResponseEntity<GameResponseDto> gameRes = client.postForEntity("/games", gameRequestDto, GameResponseDto.class);
        assertNotNull(gameRes);
        assertEquals(HttpStatus.OK, gameRes.getStatusCode());
        assertTrue(gameRes.getBody().getaGame_id() > 0, "The ID should be positive");
        this.game_id = gameRes.getBody().getaGame_id();
        assertEquals(GAME_TITLE, gameRes.getBody().getaTitle());
        assertEquals(GAME_DESCRIPTION, gameRes.getBody().getaDescription());
        assertEquals(GAME_PRICE, gameRes.getBody().getaPrice());
        assertEquals(GAME_STATUS, gameRes.getBody().getaGameStatus());
        assertEquals(GAME_STOCK_QUANTITY, gameRes.getBody().getaStockQuantity());
        assertEquals(GAME_PHOTO_URL, gameRes.getBody().getaPhotoUrl()); // Check why changing the name fixed it.

        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(REVIEW_DESCRIPTION, REVIEW_GAME_RATING, this.game_id, CUSTOMER_EMAIL);
        ResponseEntity<ReviewResponseDto> response = client.postForEntity("/reviews", reviewRequestDto, ReviewResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getReviewId() > 0, "The ID should be positive");
        this.review_id = response.getBody().getReviewId();
        assertEquals(date, response.getBody().getReviewDate());
        assertEquals(REVIEW_DESCRIPTION, response.getBody().getDescription());
        assertEquals(0, response.getBody().getRating());
        assertEquals(REVIEW_GAME_RATING, response.getBody().getGameRating());
        assertEquals(this.game_id, response.getBody().getGameId());
        assertEquals(CUSTOMER_EMAIL, response.getBody().getCustomerEmail());
    }

    @Test
    @Order(2)
    public void testGetReviewById(){
        String url = "/reviews/review/" + this.review_id;
        ResponseEntity<ReviewResponseDto> response = client.getForEntity(url, ReviewResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(this.review_id, response.getBody().getReviewId());
        assertEquals(REVIEW_DESCRIPTION, response.getBody().getDescription());
        assertEquals(0, response.getBody().getRating());
        assertEquals(REVIEW_GAME_RATING, response.getBody().getGameRating());
        assertEquals(this.game_id, response.getBody().getGameId());
        assertEquals(CUSTOMER_EMAIL, response.getBody().getCustomerEmail());
    }
    

    @Test
    @Order(3)
    public void testGetCustomerByReviewId(){
        String url = "/reviews/review/customer/" + this.review_id;
        ResponseEntity<AccountResponseDto> response = client.getForEntity(url, AccountResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CUSTOMER_EMAIL, response.getBody().getEmail());
        assertEquals(CUSTOMER_USERNAME, response.getBody().getUsername());
        assertEquals(CUSTOMER_PHONE, response.getBody().getPhoneNumber());
        assertEquals(CUSTOMER_ADDRESS, response.getBody().getAddress());
    }

    @Test
    @Order(4)
    public void testGetReviewsByGame(){
        AccountRequestDto customer2 = new AccountRequestDto("customer2@mail.test.com", "name_of_the_second", "1203021abc", "703899812", "customer 2 street");
        ResponseEntity<AccountResponseDto> customer2Response = client.postForEntity("/account/customer", customer2, AccountResponseDto.class);
        assertNotNull(customer2Response);
        assertEquals(HttpStatus.OK, customer2Response.getStatusCode());
        assertTrue("customer2@mail.test.com".equals(customer2Response.getBody().getEmail()));
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto("This is a second review of game 1", Review.GameRating.One, this.game_id, customer2.getEmail());
        ResponseEntity<ReviewResponseDto> reviewResponse = client.postForEntity("/reviews", reviewRequestDto, ReviewResponseDto.class);
        assertNotNull(reviewResponse);
        assertEquals(HttpStatus.OK, reviewResponse.getStatusCode());
        assertTrue(reviewResponse.getBody().getReviewId() > 0, "The ID should be positive");
        int review2Id = reviewResponse.getBody().getReviewId();
        String url = "/reviews/review/game" + this.game_id;
        ResponseEntity<ReviewListDto> response = client.getForEntity(url, ReviewListDto.class);
        ReviewListDto reviewListDto = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, reviewListDto.getReviews().size());
        boolean found = false;
        for(ReviewSummaryDto review : reviewListDto.getReviews()){
            if (review.getReviewId() == this.review_id){
                found = true;
                assertEquals(REVIEW_DESCRIPTION, review.getDescription());
                assertEquals(0, review.getRating());
                assertEquals(REVIEW_GAME_RATING, review.getGameRating());
                assertEquals(CUSTOMER_EMAIL, review.getCustomerEmail());
                break;
            }
        }
        assertTrue(found);
        found = false;
        for(ReviewSummaryDto review : reviewListDto.getReviews()){
            if (review.getReviewId() == review2Id){
                found = true;
                assertEquals("This is a second review of game 1", review.getDescription());
                assertEquals(0, review.getRating());
                assertEquals(GameRating.One, review.getGameRating());
                assertEquals("customer2@mail.test.com", review.getCustomerEmail());
                break;
            }
        }       
        assertTrue(found);
    }

    @Test
    @Order(5)
    public void testUpdateReview(){
        String newDescription = "This is an updated review of game 1";
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(newDescription, Review.GameRating.Five, this.game_id, CUSTOMER_EMAIL);
        ResponseEntity<ReviewResponseDto> response = client.postForEntity("/reviews", reviewRequestDto, ReviewResponseDto.class);
        this.review_id = response.getBody().getReviewId();
        String url = "/reviews/review/" + this.review_id;
        String url2 = url + "/" + 1;
        HttpEntity<ReviewRequestDto> requestEntity = new HttpEntity<>(reviewRequestDto);
        ResponseEntity<ReviewResponseDto> postResponse = client.exchange(url2, HttpMethod.PUT, requestEntity, ReviewResponseDto.class);
        assertNotNull(postResponse);
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
        assertEquals(this.review_id, postResponse.getBody().getReviewId());
        assertEquals(newDescription, postResponse.getBody().getDescription());
        assertEquals(1, postResponse.getBody().getRating());
        assertEquals(Review.GameRating.Five, postResponse.getBody().getGameRating());
        assertEquals(this.game_id, postResponse.getBody().getGameId());
        assertEquals(CUSTOMER_EMAIL, postResponse.getBody().getCustomerEmail());
    }

    // @Test
    // @Order(6)
    // public void testGetReplyById(){
    //     ReplyRequestDto replyRequest = new ReplyRequestDto(
    //         Date.valueOf(LocalDate.now()),
    //         "Thank you for your feedback!",
    //         ReviewRating.Like,
    //         this.review_id,
    //         MANAGER_EMAIL
    //     );
    //     ResponseEntity<ReplyResponseDto> replyResponse = client.postForEntity("/replies", replyRequest, ReplyResponseDto.class);
    //     ResponseEntity<ReplyResponseDto> response = client.getForEntity("/replies/" + replyResponse.getBody().getReplyId(), ReplyResponseDto.class);
    //     assertNotNull(response);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    //     assertEquals("Thank you for your feedback!", response.getBody().getDescription());
    //     assertEquals(Date.valueOf(LocalDate.now()), response.getBody().getReplyDate());
    //     assertEquals(ReviewRating.Like, response.getBody().getReviewRating());
    //     assertEquals(this.review_id, response.getBody().getReviewId());
    //     assertEquals(MANAGER_EMAIL, response.getBody().getManagerEmail());

    // }
}


