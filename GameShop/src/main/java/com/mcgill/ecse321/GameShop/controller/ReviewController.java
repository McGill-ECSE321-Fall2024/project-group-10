package com.mcgill.ecse321.GameShop.controller;

import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewRequestDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewResponseDto;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /** Create a new review */
    @PostMapping("/reviews")
    public ReviewResponseDto createReview(@Valid @RequestBody ReviewRequestDto request) {
        Review review = reviewService.createReview(
                request.getReviewDate(),
                request.getDescription(),
                request.getRating(),
                request.getGameRating(),
                request.getGameId(),
                request.getCustomerEmail()
        );
        return new ReviewResponseDto(review);
    }

    /** Get a review by ID */
    @GetMapping("/reviews/{id}")
    public ReviewResponseDto getReviewById(@PathVariable int id) {
        Review review = reviewService.getReviewById(id);
        return new ReviewResponseDto(review);
    }

    // /** Get all reviews for a game */
    // @GetMapping("/game/{gameId}")
    // public List<ReviewResponseDto> getReviewsByGame(@PathVariable int gameId) {
    //     return reviewService.getReviewsByGame(gameId).stream()
    //             .map(this::convertToDto)
    //             .collect(Collectors.toList());
    // }

    // /** Get all reviews by a customer */
    // @GetMapping("/customer/{email}")
    // public List<ReviewResponseDto> getReviewsByCustomer(@PathVariable String email) {
    //     return reviewService.getReviewsByCustomer(email).stream()
    //             .map(this::convertToDto)
    //             .collect(Collectors.toList());
    // }

    /** Update a review */
    @PutMapping("/reviews/{id}")
    public ReviewResponseDto updateReview(@PathVariable int id, @RequestBody ReviewRequestDto request) {
        Review review = reviewService.updateReview(
                id,
                request.getDescription(),
                request.getRating(),
                request.getGameRating()
        );
        return new ReviewResponseDto(review);
    }

    // /** Delete a review */
    // @DeleteMapping("/{id}")
    // public void deleteReview(@PathVariable int id) {
    //     reviewService.deleteReview(id);
    // }

}