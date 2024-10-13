package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.sql.Date;

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
    public void testCreateAndReadReview() {
       
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

        Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerPhoneNumber, customerAddress, cart);
        customer = customerRepository.save(customer);
        
        Game game = new Game(title, gameDescription, price, status, stock, url);
        game = gameRepository.save(game);

        Review review = new Review(reviewDate, reviewDescription, rating, gameRating, game, customer);
        review = reviewRepository.save(review);
        int reviewId = review.getReview_id();

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
}









