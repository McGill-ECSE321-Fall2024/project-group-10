package com.mcgill.ecse321.GameShop.dto.PromotionDto;

import java.sql.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PromotionRequestDto {

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Min(value = 0, message = "Discount rate must be between 0 and 100")
    @Max(value = 100, message = "Discount rate must be between 0 and 100")
    private int discountRate;

    @NotNull(message = "Start date cannot be null")
    private Date startDate;

    @NotNull(message = "End date cannot be null")
    private Date endDate;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Manager email cannot be empty")
    private String managerEmail;

    @NotNull(message = "Game IDs cannot be null")
    private List<Integer> gameIds;

    public PromotionRequestDto() {
    }

    public PromotionRequestDto(String description, int discountRate, Date startDate, Date endDate, String managerEmail, List<Integer> gameIds) {
        this.description = description;
        this.discountRate = discountRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managerEmail = managerEmail;
        this.gameIds = gameIds;
    }

    // Getters and Setters

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public List<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(List<Integer> gameIds) {
        this.gameIds = gameIds;
    }
}
