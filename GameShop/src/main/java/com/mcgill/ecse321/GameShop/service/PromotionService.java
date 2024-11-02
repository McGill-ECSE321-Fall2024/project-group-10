package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Promotion;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PromotionRepository;

import jakarta.transaction.Transactional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // Helper method to check if a string is empty or null
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Create a new Promotion.
     *
     * @param description  The description of the promotion.
     * @param discountRate The discount rate (0-100).
     * @param startDate    The start date of the promotion.
     * @param endDate      The end date of the promotion.
     * @param managerEmail The email of the manager creating the promotion.
     * @return The created Promotion.
     */
    @Transactional
    public Promotion createPromotion(String description, int discountRate, Date startDate, Date endDate,
            String managerEmail) {

        // Validate inputs
        if (isEmpty(description)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Description cannot be empty or null");
        }
        if (discountRate < 0 || discountRate > 100) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Discount rate must be between 0 and 100");
        }
        if (startDate == null || endDate == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Start date and end date cannot be null");
        }
        if (startDate.after(endDate)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
        }
        Manager manager = managerRepository.findByEmail(managerEmail);
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND,
                    String.format("Manager with email %s not found", managerEmail));
        }

        // Create the promotion
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);

        // Save and return the promotion
        return promotionRepository.save(promotion);
    }

    /**
     * Retrieve a Promotion by its ID.
     *
     * @param promotionId The ID of the promotion.
     * @return The Promotion object.
     */
    @Transactional
    public Promotion getPromotionById(int promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId);
        if (promotion == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Promotion not found");
        }
        return promotion;
    }

    /**
     * Retrieve all Promotions.
     *
     * @return An iterable of all Promotions.
     */
    @Transactional
    public Iterable<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    /**
     * Update an existing Promotion.
     *
     * @param promotionId  The ID of the promotion to update.
     * @param description  The new description (optional).
     * @param discountRate The new discount rate (optional).
     * @param startDate    The new start date (optional).
     * @param endDate      The new end date (optional).
     * @return The updated Promotion.
     */
    @Transactional
    public Promotion updatePromotion(int promotionId, String description, Integer discountRate, Date startDate,
            Date endDate) {
        Promotion promotion = getPromotionById(promotionId);

        // Update description if provided
        if (!isEmpty(description)) {
            promotion.setDescription(description);
        }

        // Update discount rate if provided
        if (discountRate != null) {
            if (discountRate < 0 || discountRate > 100) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Discount rate must be between 0 and 100");
            }
            promotion.setDiscountRate(discountRate);
        }

        // Update dates if provided
        if (startDate != null) {
            if (endDate != null && startDate.after(endDate)) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
            }
            promotion.setStartDate(startDate);
        }
        if (endDate != null) {
            if (startDate != null && startDate.after(endDate)) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Start date cannot be after end date");
            }
            promotion.setEndDate(endDate);
        }

        // Save and return the updated promotion
        return promotionRepository.save(promotion);
    }
}