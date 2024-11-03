package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Review.GameRating;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CustomerRepository customerRepository;

    // Helper method to check if a string is empty or null
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Create a new Review.
     *
     * @param reviewDate    The date of the review.
     * @param description   The description of the review.
     * @param rating        The numerical rating of the review (1-5).
     * @param gameRating    The game rating enum (One to Five).
     * @param gameId        The ID of the game being reviewed.
     * @param customerEmail The email of the customer creating the review.
     * @return The created Review.
     */
    @Transactional
    public Review createReview(Date reviewDate, String description, int rating, GameRating gameRating, int gameId, String customerEmail) {

        // Validate inputs
        if (reviewDate == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Review date cannot be null");
        }
        if (isEmpty(description)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Description cannot be empty or null");
        }
        if (rating < 1 || rating > 5) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }
        if (gameRating == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game rating cannot be null");
        }

        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d not found", gameId));
        }

        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Customer with email %s not found", customerEmail));
        }

        // Create the review
        Review review = new Review(reviewDate, description, rating, gameRating, game, customer);

        // Save and return the review
        return reviewRepository.save(review);
    }

    /**
     * Retrieve a Review by its ID.
     *
     * @param reviewId The ID of the review.
     * @return The Review object.
     */
    @Transactional
    public Review getReviewById(int reviewId) {
        Review review = reviewRepository.findById(reviewId);
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Review not found");
        }
        return review;
    }

    /**
     * Retrieve all Reviews.
     *
     * @return An iterable of all Reviews.
     */
    @Transactional
    public Iterable<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * Update an existing Review.
     *
     * @param reviewId    The ID of the review to update.
     * @param description The new description (optional).
     * @param rating      The new numerical rating (optional).
     * @param gameRating  The new game rating enum (optional).
     * @return The updated Review.
     */
    @Transactional
    public Review updateReview(int reviewId, String description, Integer rating, GameRating gameRating) {
        Review review = getReviewById(reviewId);

        // Update description if provided
        if (!isEmpty(description)) {
            review.setDescription(description);
        }

        // Update rating if provided
        if (rating != null) {
            if (rating < 1 || rating > 5) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
            }
            review.setRating(rating);
        }

        // Update game rating if provided
        if (gameRating != null) {
            review.setGameRating(gameRating);
        }

        // Save and return the updated review
        return reviewRepository.save(review);
    }
    
}