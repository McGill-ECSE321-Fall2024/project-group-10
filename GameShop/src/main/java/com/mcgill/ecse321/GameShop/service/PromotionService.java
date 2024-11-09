package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Promotion;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.PromotionRepository;

import jakarta.transaction.Transactional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private GameRepository gameRepository;

    // Helper method to check if a string is empty or null
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Create a new Promotion.
     */
    @Transactional
    public Promotion createPromotion(String description, int discountRate, Date startDate, Date endDate,
            String managerEmail, List<Integer> gameIds) {

        // Validate inputs
        if (gameIds == null || gameIds.isEmpty()) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game IDs cannot be null or empty");
        }

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

        // Fetch games by IDs
        List<Game> games = new ArrayList<>();
        for (int  gameId : gameIds) {
            Game game = gameRepository.findById(gameId);
            if (game == null){
                    throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d not found", gameId));
            }
            games.add(game);
        }

        // Create the promotion with games
        Promotion promotion = new Promotion(description, discountRate, startDate, endDate, manager);
        promotion.setGames(games);

        // Save and return the promotion
        return promotionRepository.save(promotion);
    }

    /**
     * Retrieve a Promotion by its ID.
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
     */
    @Transactional
    public Iterable<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    /**
     * Update an existing Promotion.
     */
    @Transactional
    public Promotion updatePromotion(int promotionId, String description, Integer discountRate, Date startDate,
            Date endDate, List<Integer> gameIds) {
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

        // Update associated games if provided
        if (gameIds != null) {
            List<Game> games = new ArrayList<>();
            for (int  gameId : gameIds) {
                Game game = gameRepository.findById(gameId);
                if (game == null){
                        throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d not found", gameId));
                }
                games.add(game);
            }
            promotion.setGames(games);
        }

        // Save and return the updated promotion
        return promotionRepository.save(promotion);
    }

    /**
     * Delete a Promotion by its ID.
     */
    @Transactional
    public void deletePromotion(int promotionId) {
        Promotion promotion = getPromotionById(promotionId);
        promotionRepository.delete(promotion);
    }

    /**
     * Get all games associated with a promotion.
     */
    @Transactional
    public List<Game> getAllGamesFromPromotion(int promotionId) {
        Promotion promotion = getPromotionById(promotionId);
        return promotion.getGames();
    }

    /**
     * Get a specific game by ID from a promotion.
     */
    @Transactional
    public Game getGameByIdFromPromotion(int promotionId, int gameId) {
        Promotion promotion = getPromotionById(promotionId);
        for (Game game : promotion.getGames()) {
            if (game.getGame_id() == gameId) {
                return game;
            }
        }
        throw new GameShopException(HttpStatus.NOT_FOUND,
                String.format("Game with ID %d not found in Promotion %d", gameId, promotionId));
    }

    /**
     * Add a game to a promotion.
     */
    @Transactional
    public Promotion addGameToPromotion(int promotionId, int gameId) {
        Promotion promotion = getPromotionById(promotionId);
        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d not found", gameId));
        }
        if (!promotion.getGames().contains(game)) {
            promotion.getGames().add(game);
            promotionRepository.save(promotion);
        }
        return promotion;
    }

    /**
     * Remove a game from a promotion.
     */
    @Transactional
    public Promotion removeGameFromPromotion(int promotionId, int gameId) {
        Promotion promotion = getPromotionById(promotionId);
        Game game = gameRepository.findById(gameId);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d not found", gameId));
        }
        if (promotion.getGames().contains(game)) {
            promotion.getGames().remove(game);
            promotionRepository.save(promotion);
        }
        return promotion;
    }
}