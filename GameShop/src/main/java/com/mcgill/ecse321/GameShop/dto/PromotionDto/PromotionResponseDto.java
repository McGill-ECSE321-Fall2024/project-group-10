package com.mcgill.ecse321.GameShop.dto.PromotionDto;

import java.sql.Date;

import com.mcgill.ecse321.GameShop.model.Promotion;

public class PromotionResponseDto {

    private int promotionId;
    private String description;
    private int discountRate;
    private Date startDate;
    private Date endDate;
    private String managerEmail;
    //private List<GameSummaryDto> games;

    public PromotionResponseDto() {
    }

    public PromotionResponseDto(Promotion promotion) {
        this.promotionId = promotion.getPromotion_id();
        this.description = promotion.getDescription();
        this.discountRate = promotion.getDiscountRate();
        this.startDate = promotion.getStartDate();
        this.endDate = promotion.getEndDate();
        this.managerEmail = promotion.getManager().getEmail();
        // Convert associated games to GameSummaryDto
        //this.games = promotion.getGames().stream()
                //.map(GameSummaryDto::new)
                //.collect(Collectors.toList());
    }

    // Getters and Setters

    public int getPromotionId() {
        return promotionId;
    }

    public String getDescription() {
        return description;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    //public List<GameSummaryDto> getGames() {
    //    return games;
    //}
}

