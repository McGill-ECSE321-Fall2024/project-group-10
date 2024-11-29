package com.mcgill.ecse321.GameShop.dto.PromotionDto;

import com.mcgill.ecse321.GameShop.model.Promotion;

public class PromotionSummaryDto {

    private int promotionId;
    private String description;
    private int discountRate;

    public PromotionSummaryDto() {
    }

    public PromotionSummaryDto(Promotion promotion) {
        this.promotionId = promotion.getPromotion_id();
        this.description = promotion.getDescription();
        this.discountRate = promotion.getDiscountRate();
    }

    // Getters

    public int getPromotionId() {
        return promotionId;
    }

    public String getDescription() {
        return description;
    }

    public int getDiscountRate() {
        return discountRate;
    }
}
