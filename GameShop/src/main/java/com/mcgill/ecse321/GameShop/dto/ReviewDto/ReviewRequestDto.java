package com.mcgill.ecse321.GameShop.dto.ReviewDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.sql.Date;

import com.mcgill.ecse321.GameShop.model.Review.GameRating;

public class ReviewRequestDto {

    @NotNull(message = "Review date cannot be null")
    private Date reviewDate;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;

    @NotNull(message = "Game rating cannot be null")
    private GameRating gameRating;

    @NotNull(message = "Game ID cannot be null")
    private Integer gameId;

    @NotBlank(message = "Customer email cannot be empty")
    private String customerEmail;

    public ReviewRequestDto() {
    }

    public ReviewRequestDto(Date reviewDate, String description, int rating, GameRating gameRating, Integer gameId, String customerEmail) {
        this.reviewDate = reviewDate;
        this.description = description;
        this.rating = rating;
        this.gameRating = gameRating;
        this.gameId = gameId;
        this.customerEmail = customerEmail;
    }

    // Getters and Setters

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public GameRating getGameRating(){
        return gameRating;
    }

    public void setGameRating(GameRating gameRating){
        this.gameRating = gameRating;
    }

    public Integer getGameId(){
        return gameId;
    }

    public void setGameId(Integer gameId){
        this.gameId = gameId;
    }

    public String getCustomerEmail(){
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail){
        this.customerEmail = customerEmail;
    }
}
