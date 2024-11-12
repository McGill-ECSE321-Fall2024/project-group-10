package com.mcgill.ecse321.GameShop.controller;

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyResponseDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewListDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewRequestDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewResponseDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewSummaryDto;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Reply;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Review.GameRating;
import com.mcgill.ecse321.GameShop.service.ReviewService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                request.getDescription(),
                request.getGameRating(),
                request.getGameId(),
                request.getCustomerEmail()
        );
        
        return new ReviewResponseDto(review);
    }

    /** Get a review by ID */
    @GetMapping("/reviews/review/{id}")
    public ReviewResponseDto getReviewById(@PathVariable int id) {
        Review review = reviewService.getReviewById(id);
        return new ReviewResponseDto(review);
    }
    

    /** Get the customer that wrote the review*/
    @GetMapping("/reviews/review/customer/{id}")
    public AccountResponseDto getCustomerByReviewId(@PathVariable int id) {
        Customer customer = reviewService.getCustomerByReviewID(id);
        return new AccountResponseDto(customer);
    }
    
    /** Get all the reviews to a Game */
    @GetMapping("/reviews/review/game{gameId}")
    public ReviewListDto getReviewsByGame(@PathVariable int gameId) {
        Iterable<Review> reviews = reviewService.getReviewsByGame(gameId);
        List<ReviewSummaryDto> reviewsDto = new ArrayList<>();
        for (Review review : reviews) {
            reviewsDto.add(new ReviewSummaryDto(review));
        }
        return new ReviewListDto(reviewsDto);
        }

    /** Get all the reviews done by a customer */
    @GetMapping("/reviews/customer/r{email}")
    public ReviewListDto getReviewsByCustomer(@PathVariable String email) {
        Iterable<Review> reviews = reviewService.getReviewsByCustomer(email);
        List<ReviewSummaryDto> reviewsDto = new ArrayList<>();
        for (Review review : reviews) {
            reviewsDto.add(new ReviewSummaryDto(review));
        }
        return new ReviewListDto(reviewsDto);
    }

    /** Get the reply to a review */
    @GetMapping("/reviews/review/reply/{reviewId}")
    public ReplyResponseDto getReplyByReviewId(@PathVariable int reviewId) {
        Reply reply = reviewService.getReplyToReview(reviewId);
        return new ReplyResponseDto(reply);
    }


    @PutMapping("/reviews/review/{id}/{rating}")
    public ReviewResponseDto updateRating(@PathVariable int id, @PathVariable int rating) {
        Review review = reviewService.updateReviewRating(id, rating);
        return new ReviewResponseDto(review);
    }
}