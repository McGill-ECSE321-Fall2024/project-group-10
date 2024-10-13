package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.valueOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Reply;
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
    private AccountRepository accountRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        accountRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndReadReview() {
       
        String email = "tarek.elakkaoui@gmail.ca";
        String username = "TarekEl-Akkaoui";
        String password = "pass";
        String phoneNumber = "+1 (438) 865-9292";
        String address = "0000 rue Liban";

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

        String customerEmail = "anthony.saber@hotmail.uk";
        String customerUsername = "AnthonySaberrrrr";
        String customerPassword = "pass";
        String customerPhoneNumber = "+1 (438) 865-9282";
        String customerAddress = "321 rue Canada";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerPhoneNumber, customerAddress, cart);
        customer = accountRepository.save(customer);
        
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
        }       
}









