package com.mcgill.ecse321.GameShop.dto.ReviewDto;

import com.mcgill.ecse321.GameShop.model.Review;

public class ReviewSummaryDto {

    private int reviewId;
    private String description;
    private int rating;

    public ReviewSummaryDto(Review review) {
        this.reviewId = review.getReview_id();
        this.description = review.getDescription();
        this.rating = review.getRating();
    }

    // Getters

    public int getReviewId() {
        return reviewId;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }
}
