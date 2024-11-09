package com.mcgill.ecse321.GameShop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Review.GameRating;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ReviewRepository;


@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks ReviewService reviewService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private GameRepository gameRepository;  

    private static int INVALID_RATING;
    private static int INVALID_REVIEW_ID;
    private static int INVALID_GAME_ID;
    private static int VALID_REVIEW_ID;
    private static final String VALID_DESCRIPTION = "This is a valid description";
    private static final GameRating VALID_GAME_RATING = GameRating.Five;
    private static int VALID_RATING ;
    private static  int VALID_GAME_ID;
    private static final String VALID_CUSTOMER_EMAIL = "validcustomer@mail.com";
    private static final String INVALID_CUSTOMER_EMAIL = "invalidmail@gmail.com";
    private static final Customer customer = new Customer(VALID_CUSTOMER_EMAIL,"customerUserm","password","37465","montral",new Cart());
    private static final Game game = new Game("title", "VALID_DESCRIPTION", 5, GameStatus.InStock, 10, "urls");


    @Test
    public void testCreateReview() {
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 10000;
        
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(customer);
        when(reviewRepository.save(any(Review.class))).thenAnswer((InvocationOnMock invocation) -> {
            Review review = invocation.getArgument(0);
            review.setReview_id(VALID_REVIEW_ID);
            return review;
        });
        
        Review createdReview = reviewService.createReview(VALID_DESCRIPTION,
         VALID_GAME_RATING, VALID_GAME_ID,VALID_CUSTOMER_EMAIL);
        assertNotNull(createdReview);
        assertEquals(VALID_DESCRIPTION, createdReview.getDescription());
        assertEquals(VALID_GAME_RATING, createdReview.getGameRating());
        assertEquals(VALID_RATING, createdReview.getRating());
        assertEquals(VALID_GAME_ID, createdReview.getGame().getGame_id());
        assertEquals(VALID_CUSTOMER_EMAIL, createdReview.getCustomer().getEmail());
        assertEquals(VALID_RATING, createdReview.getRating());
    }
    
    @Test
    public void testCreatReviewNullDescrition(){
        VALID_REVIEW_ID = 3;
        VALID_RATING = 0;
        VALID_GAME_ID = 2;
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(null, VALID_GAME_RATING, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test 
    public void testCreateReviewEmptyDescription(){
        VALID_REVIEW_ID = 4;
        VALID_RATING = 0;
        VALID_GAME_ID = 3;
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview("", VALID_GAME_RATING, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    public void testCreateReviewNullGameRating(){
        VALID_REVIEW_ID = 9;
        VALID_RATING = 0;
        VALID_GAME_ID = 7;
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(VALID_DESCRIPTION, null, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game rating cannot be null", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    public void testCreateReviewNotPositiveGameId(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 10;
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, -1, VALID_CUSTOMER_EMAIL);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Game ID must be positive", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }
   
    @Test 
    public void testCreateReviewInvalidGameId(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 100;
        INVALID_GAME_ID = 8;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(INVALID_GAME_ID)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, INVALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Game with ID 8 not found", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    
    }

    @Test
    public void testCreateReviewNullCustomerEmail(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 200;
        INVALID_GAME_ID = 8;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, null);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email cannot be empty or null", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test
    public void testCreateReviewEmptyCustomerEmail(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 201;
        INVALID_GAME_ID = 8;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, "");
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email cannot be empty or null", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test 
    public void testCreateReviewInvalidCustomerEmail(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 203;
        INVALID_GAME_ID = 8;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(customerRepository.findByEmail(INVALID_CUSTOMER_EMAIL)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, INVALID_CUSTOMER_EMAIL);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Customer with email invalidmail@gmail.com not found", exception.getMessage());
        verify(reviewRepository, times(0)).save(any(Review.class));
    }

    @Test 
    public void testFindByReviewId(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 203;
        INVALID_GAME_ID = 8;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(customer);
        when(reviewRepository.save(any(Review.class))).thenAnswer((InvocationOnMock invocation) -> {
            Review review = invocation.getArgument(0);
            review.setReview_id(VALID_REVIEW_ID);
            return review;
        });
        Review review = reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        when(reviewRepository.findById(VALID_REVIEW_ID)).thenReturn(review);
        Review foundReview = reviewService.getReviewById(VALID_REVIEW_ID);
        assertNotNull(foundReview);
        assertEquals(VALID_DESCRIPTION, foundReview.getDescription());
        assertEquals(VALID_GAME_RATING, foundReview.getGameRating());
        assertEquals(VALID_RATING, foundReview.getRating());
        assertEquals(VALID_GAME_ID, foundReview.getGame().getGame_id());
        assertEquals(VALID_CUSTOMER_EMAIL, foundReview.getCustomer().getEmail());
        assertEquals(VALID_RATING, foundReview.getRating());
    }

    @Test
    public void testFindByInvalidReviewId(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 203;
        INVALID_GAME_ID = 8;
        INVALID_REVIEW_ID = -25;
        game.setGame_id(VALID_GAME_ID);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.getReviewById(INVALID_REVIEW_ID);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Review ID must be positive", exception.getMessage());
    }

    @Test
    public void testFindByReviewIdNotFound(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 203;
        INVALID_GAME_ID = 8;
        INVALID_REVIEW_ID = 25;
        game.setGame_id(VALID_GAME_ID);
        when(reviewRepository.findById(INVALID_REVIEW_ID)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.getReviewById(INVALID_REVIEW_ID);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    public void testGetCustomerFromReviewId  (){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 204;
        INVALID_GAME_ID = 8;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(customer);
        when(reviewRepository.save(any(Review.class))).thenAnswer((InvocationOnMock invocation) -> {
            Review review = invocation.getArgument(0);
            review.setReview_id(VALID_REVIEW_ID);
            return review;
        });
        Review review = reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        when(reviewRepository.findById(VALID_REVIEW_ID)).thenReturn(review);
        Customer customer = reviewService.getCustomerByReviewID(VALID_REVIEW_ID);
        assertNotNull(customer);
        assertEquals(VALID_CUSTOMER_EMAIL, customer.getEmail());
    }  

    @Test
    public void testGetCustormerFromInvalidReviewId(){
        VALID_REVIEW_ID = 1;
        VALID_RATING = 0;
        VALID_GAME_ID = 204;
        INVALID_GAME_ID = 8;
        INVALID_REVIEW_ID = -25;
        game.setGame_id(VALID_GAME_ID);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.getCustomerByReviewID(INVALID_REVIEW_ID);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Review ID must be positive", exception.getMessage());
    }

    @Test
    public void testGetCustomerFromReviewIdNotFound(){
        VALID_REVIEW_ID = 12;
        VALID_RATING = 340;
        VALID_GAME_ID = 209;
        INVALID_GAME_ID = 87;
        INVALID_REVIEW_ID = 589;
        game.setGame_id(VALID_GAME_ID);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.getCustomerByReviewID(INVALID_REVIEW_ID);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    public void testUpdateReviewRating(){
        VALID_REVIEW_ID = 333;
        VALID_RATING = 1;
        VALID_GAME_ID = 2991;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(customer);
        when(reviewRepository.save(any(Review.class))).thenAnswer((InvocationOnMock invocation) -> {
            Review review = invocation.getArgument(0);
            review.setReview_id(VALID_REVIEW_ID);
            return review;
        });
        Review review = reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        when(reviewRepository.findById(VALID_REVIEW_ID)).thenReturn(review);
        Review updatedReview = reviewService.updateReviewRating(VALID_REVIEW_ID, VALID_RATING);
        assertNotNull(updatedReview);
        assertEquals(VALID_DESCRIPTION, updatedReview.getDescription());
        assertEquals(VALID_GAME_RATING, updatedReview.getGameRating());
        assertEquals(VALID_RATING, updatedReview.getRating());
        assertEquals(VALID_GAME_ID, updatedReview.getGame().getGame_id());
        assertEquals(VALID_CUSTOMER_EMAIL, updatedReview.getCustomer().getEmail());
        assertEquals(VALID_RATING, updatedReview.getRating());
    }

    @Test
    public void testUpdateReviewRating2(){
        VALID_REVIEW_ID = 332;
        VALID_RATING = -1;
        VALID_GAME_ID = 2973;
        game.setGame_id(VALID_GAME_ID);
        when(gameRepository.findById(VALID_GAME_ID)).thenReturn(game);
        when(customerRepository.findByEmail(VALID_CUSTOMER_EMAIL)).thenReturn(customer);
        when(reviewRepository.save(any(Review.class))).thenAnswer((InvocationOnMock invocation) -> {
            Review review = invocation.getArgument(0);
            review.setReview_id(VALID_REVIEW_ID);
            return review;
        });
        Review review = reviewService.createReview(VALID_DESCRIPTION, VALID_GAME_RATING, VALID_GAME_ID, VALID_CUSTOMER_EMAIL);
        when(reviewRepository.findById(VALID_REVIEW_ID)).thenReturn(review);
        Review updatedReview = reviewService.updateReviewRating(VALID_REVIEW_ID, VALID_RATING);
        assertNotNull(updatedReview);
        assertEquals(VALID_DESCRIPTION, updatedReview.getDescription());
        assertEquals(VALID_GAME_RATING, updatedReview.getGameRating());
        assertEquals(VALID_RATING, updatedReview.getRating());
        assertEquals(VALID_GAME_ID, updatedReview.getGame().getGame_id());
        assertEquals(VALID_CUSTOMER_EMAIL, updatedReview.getCustomer().getEmail());
        assertEquals(VALID_RATING, updatedReview.getRating());
    }
    
    @Test
    public void testUpdateReviewRatingInvalidReviewId(){
        VALID_REVIEW_ID = 3327;
        VALID_RATING = -1;
        VALID_GAME_ID = 2971;
        INVALID_REVIEW_ID = -1091;
        game.setGame_id(VALID_GAME_ID);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.updateReviewRating(INVALID_REVIEW_ID, VALID_RATING);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Review ID must be positive", exception.getMessage());
    }

    @Test
    public void testUpdateReviewRatingInvalidRating(){
        VALID_REVIEW_ID = 3328;
        VALID_RATING = 1;
        VALID_GAME_ID = 2972;
        INVALID_REVIEW_ID = 1092;
        INVALID_RATING = 192;
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.updateReviewRating(INVALID_REVIEW_ID, INVALID_RATING);
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Rating must be either 1 or -1", exception.getMessage());
    }
    
    @Test
    public void testUpdateReviewRatingNotFound(){
        VALID_REVIEW_ID = 3329;
        VALID_RATING = 1;
        VALID_GAME_ID = 2973;
        INVALID_REVIEW_ID = 1093;
        game.setGame_id(VALID_GAME_ID);
        when(reviewRepository.findById(INVALID_REVIEW_ID)).thenReturn(null);
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            reviewService.updateReviewRating(INVALID_REVIEW_ID, VALID_RATING);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Review not found", exception.getMessage());
    }
    @Test
    public void testGetReviewsByGame() {
        VALID_GAME_ID = 2974;

        Game game1 = new Game("Game1", "Description1", 50, Game.GameStatus.InStock, 10, "photo1.jpg");
        Customer customer1 = new Customer("customer1@mail.com", "customer1", "pass", "123321", "helloMtl", new Cart());
        Customer customer2 = new Customer("customer2@mail.com", "customer2", "passwors1", "1233212345", "byeMtl", new Cart());
        Date today = Date.valueOf(LocalDate.now());
        game1.setGame_id(VALID_GAME_ID);
        Review review1 = new Review(today, "desc1",0, GameRating.Five, game1, customer1);
        Review review2 = new Review(today, "desc2",0, GameRating.Four, game1, customer2);
        review1.setReview_id(1);
        review2.setReview_id(2);
        when(reviewRepository.findAll()).thenReturn(java.util.Arrays.asList(review1, review2));
        Iterable<Review> reviews = reviewService.getReviewsByGame(VALID_GAME_ID);
        assertNotNull(reviews);
        assertEquals(2, ((ArrayList<Review>) reviews).size());
        boolean foundReview = false;
        for (Review review : reviews) {
            if(review.getReview_id() == 1) {
                foundReview = true;
                assertEquals("desc1", review.getDescription());
                assertEquals(GameRating.Five, review.getGameRating());
                assertEquals(0, review.getRating());
                assertEquals(VALID_GAME_ID, review.getGame().getGame_id());
            }
        }
        assertTrue(foundReview);
        foundReview = false;
        for (Review review : reviews) {
            if(review.getReview_id() == 2) {
                foundReview = true;
                assertEquals("desc2", review.getDescription());
                assertEquals(GameRating.Four, review.getGameRating());
                assertEquals(0, review.getRating());
                assertEquals(VALID_GAME_ID, review.getGame().getGame_id());
            }
        }
        assertTrue(foundReview);
        boolean foundWrongReview = false;
        for (Review review : reviews) {
            if(review.getReview_id() == 3) {
                foundWrongReview = true;
            }
        }
        assertFalse(foundWrongReview);
    }

    @Test
    public void testGetReviewsByGameNotFound() {
        VALID_GAME_ID = 2975;
        when(reviewRepository.findAll()).thenReturn(new ArrayList<Review>());
        Iterable<Review> reviews = reviewService.getReviewsByGame(VALID_GAME_ID);
        assertNotNull(reviews);
        assertEquals(0, ((ArrayList<Review>) reviews).size());
    }
}