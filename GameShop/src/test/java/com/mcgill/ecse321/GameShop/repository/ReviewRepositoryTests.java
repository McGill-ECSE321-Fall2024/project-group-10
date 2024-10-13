package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Review.GameRating;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndReadReview() {
        // Create necessary objects
        String customerEmail = "tarek.elakkaoui@gmail.ca";
        String customerUsername = "TarekEl-Akkaoui";
        String customerPassword = "pass";
        String customerPhoneNumber = "+1 (438) 865-9292";
        String customerAddress = "0000 rue Liban";

        String title = "Tetris";
        String gameDescription = "A block puzzle game";
        GameStatus status = GameStatus.InStock;
        int stock = 11;
        String url = "www.randomliiiiiink.ca";
        int price = 70;

        Date reviewDate = Date.valueOf("2024-10-05");
        int rating = 0;
        GameRating gameRating = GameRating.Five;
        String reviewDescription = "This is a very good game";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerPhoneNumber,
                customerAddress, cart);
        customer = customerRepository.save(customer);

        Game game = new Game(title, gameDescription, price, status, stock, url);
        game = gameRepository.save(game);

        Review review = new Review(reviewDate, reviewDescription, rating, gameRating, game, customer);
        review = reviewRepository.save(review);
        int reviewId = review.getReview_id();

        // Get Review fro the DB
        Review pulledReview = reviewRepository.findById(reviewId);

        // Assertions
        assertNotNull(pulledReview);
        assertEquals(review.getDescription(), pulledReview.getDescription());
        assertEquals(review.getReviewDate(), pulledReview.getReviewDate());
        assertEquals(review.getGameRating(), pulledReview.getGameRating());
        assertEquals(review.getGame().getGame_id(), pulledReview.getGame().getGame_id());
        assertEquals(customer.getEmail(), pulledReview.getCustomer().getEmail());
        assertEquals(review.getRating(), pulledReview.getRating());
        assertTrue(review.getCustomer() instanceof Customer, "The reviewer account should be a customer.");
    }

    @Test
    @Transactional
    public void testCreateMultipleReviewsForGame() {
        // Create necessary objects
        Cart cart2 = new Cart();
        cart2 = cartRepository.save(cart2);

        Cart cart3 = new Cart();
        cart3 = cartRepository.save(cart3);

        Customer customer1 = new Customer("201@201.com", "201", "password",
                "+1 (201) 201-7890", "201 rue Crescent", cart2);
        customer1 = customerRepository.save(customer1);

        Customer customer2 = new Customer("202@202.com", "202", "password",
                "+1 (202) 765-2022", "202 rue Bishop", cart3);
        customer2 = customerRepository.save(customer2);

        Game game = new Game("Superman", "An interesting game", 50, GameStatus.InStock,
                10, "");
        game = gameRepository.save(game);

        // Create first review
        Date reviewDate1 = Date.valueOf("2024-10-05");
        GameRating gameRating1 = GameRating.Five;
        String reviewDescription1 = "This is a very good game";

        Review review1 = new Review(reviewDate1, reviewDescription1, 0, gameRating1, game, customer1);
        review1 = reviewRepository.save(review1);

        // Create second review
        Date reviewDate2 = Date.valueOf("2024-10-06");
        GameRating gameRating2 = GameRating.Four;
        String reviewDescription2 = "Enjoyed the game a lot";

        Review review2 = new Review(reviewDate2, reviewDescription2, 0, gameRating2, game, customer2);
        review2 = reviewRepository.save(review2);

        // Retrieve all reviews
        Iterable<Review> allReviews = reviewRepository.findAll();

        // Filter reviews associated with the specific game
        List<Review> reviewsForGame = new ArrayList<>();

        // Null Assertion before adding reviews
        assertNotNull(reviewsForGame, "Reviews list should not be null.");

        for (Review review : allReviews) {
            if (review.getGame().getGame_id() == game.getGame_id()) {
                reviewsForGame.add(review);
            }
        }

        // Assertions
        assertEquals(2, reviewsForGame.size(), "There should be 2 reviews associated with the game.");

        // Verify that both reviews are present in the retrieved list
        assertTrue(reviewsForGame.contains(review1), "First review should be associated with the game.");
        assertTrue(reviewsForGame.contains(review2), "Second review should be associated with the game");
    }
}