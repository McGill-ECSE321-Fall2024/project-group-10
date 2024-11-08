package com.mcgill.ecse321.GameShop.service;

import java.util.ArrayList;
import java.beans.Transient;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;

import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.CategoryRepository;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.PlatformRepository;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PlatformRepository platformRepository;

    public boolean isEmpty(String str){
        return str == null || str.trim().isEmpty();
    }
    
    @Transactional
    public Game createGame(String title, String aDescription, int aPrice, Game.GameStatus aGameStatus, int aStockQuantity, String aPhotoUrl) {
        // if (isEmpty(aPhotoUrl) || isEmpty(title) || isEmpty(aDescription) || aPrice < 0 || aStockQuantity < 0 || aGameStatus == null) {
        //     throw new GameShopException(HttpStatus.BAD_REQUEST, "Constructor fields for Game cannot be empty, null, or negative");
        // }
        if (isEmpty(title)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Title cannot be empty or null");
        }
        if (isEmpty(aDescription)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Description cannot be empty or null");
        }
        if (aPrice <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Price cannot be negative or 0");
        }
        if (aStockQuantity < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative");
        }
        if (aGameStatus == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game status cannot be null");
        }
        if (isEmpty(aPhotoUrl)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Photo URL cannot be empty or null");
        }
        Game game = new Game(title, aDescription, aPrice, aGameStatus, aStockQuantity, aPhotoUrl);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game findGameById(int game_id) {
        if (game_id <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = gameRepository.findById(game_id);
        if (game == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, String.format("Game with ID %d does not exist", game_id));
        }
        return game;
    }

    @Transactional
    public Iterable<Game> getGamesByTitle(String title) {
        if (isEmpty(title)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Title cannot be empty or null");
        }
        Iterable<Game> games = gameRepository.findAllByTitle(title);
        if (!games.iterator().hasNext()) { // Checks if games list is empty
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with title %s does not exist", title));
        }
        return games;
        // PAST METHOD
        // Iterable<Game> games = this.getAllGames();
        // for (Game game : games) {
        //     if (game.getTitle().equals(title)) {
        //         return games;
        //     }
        // }
        // throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with title %s does not exist", title));
    }

    @Transactional 
    public Iterable<Game> getGamesByCategory(Category category){
        if(category == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Category cannot be null");
        }
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByCategory = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getCategories().contains(category)) {
                gamesByCategory.add(game);
            }
        }
        if (gamesByCategory.isEmpty()) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "No games with the given category");
        }
        return gamesByCategory;
    }

    @Transactional
    public Iterable<Game> getGamesByPlatform(Platform platform) {
        if (platform == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform cannot be null");
        }
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByPlatform = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getPlatforms().contains(platform)) {
                gamesByPlatform.add(game);
            }
        }
        if (gamesByPlatform.isEmpty()) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "No games with the given platform");
        }
        return gamesByPlatform;
    }

    @Transactional
    public Iterable<Game> getGamesByStatus(GameStatus status){
        if(status == null){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game status cannot be null");
        }
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByStatus = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getGameStatus().equals(status)) {
                gamesByStatus.add(game);
            }
        }
        if(gamesByStatus.isEmpty()){
            throw new GameShopException(HttpStatus.NOT_FOUND, "No games with the given status");
        }
        return gamesByStatus;
    }
    

    @Transactional
    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Transactional
    public Game updateGameDescription(int game_id, String newDescription) {
        if(isEmpty(newDescription)){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Description cannot be empty or null");
        }
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(game_id);

        game.setDescription(newDescription);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGamePrice(int game_id, int newPrice) {
        if (newPrice <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Price cannot be negative nor null");
        }
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(game_id);

        game.setPrice(newPrice);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGameStockQuantity(int game_id, int newStockQuantity) {
        if (newStockQuantity < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative");
        }
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(game_id);

        game.setStockQuantity(newStockQuantity);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGameStatus(int game_id, Game.GameStatus newGameStatus) {
        if (newGameStatus == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game status cannot be null");
        }
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(game_id);

        game.setGameStatus(newGameStatus);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGamePhotoUrl(int game_id, String newPhotoUrl) {
        if (isEmpty(newPhotoUrl)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Photo URL cannot be empty or null");
        }
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(game_id);

        game.setPhotoUrl(newPhotoUrl);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGameTitle(int game_id, String newTitle) {
        if (isEmpty(newTitle)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Title cannot be empty or null");
        }
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(game_id);

        game.setTitle(newTitle);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateCategories(int gameId, List<Integer> categories) {
       
        if (categories == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Categories cannot be null");
        }
        if (gameId <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(gameId);
  
        for (int category_id : categories) {
            if (category_id <= 0) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Invalid category ID");
            }
            Category category = categoryRepository.findById(category_id);
            if (category == null) {
                throw new GameShopException(HttpStatus.NOT_FOUND,"Category does not exist");
            }
            if (!game.getCategories().contains(category)) {
                game.addCategory(category);
            }
       
          
        }
      
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updatePlatforms(int gameId, List<Integer> platforms) {
        if (platforms == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platforms cannot be null");
        }
        if (gameId <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        Game game = findGameById(gameId);
        for (int platform_id : platforms) {
            if (platform_id <= 0) {
                throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform ID must be greater than 0");
            }
            Platform platform = platformRepository.findById(platform_id);
            if (platform == null) {
                throw new GameShopException(HttpStatus.NOT_FOUND, "Platform does not exist");
            }
            if(!game.getPlatforms().contains(platform)){
                game.addPlatform(platform);
            }

        }
        gameRepository.save(game);
        return game;
    }
    
    @Transactional 
    public void deleteGame(int game_id) {
        Game game = findGameById(game_id);
        gameRepository.delete(game);

    }

    @Transactional
    public boolean setCategory(int game_id, int category_id) {
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        if(category_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Category ID must be greater than 0");
        }
        Game game = findGameById(game_id);
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
        }

        Category category = categoryRepository.findById(category_id);
        if (category == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Category with ID %d does not exist", category_id));
        }
        if (game.getCategories().contains(category)) {
            return game.removeCategory(category);
        }
        return game.addCategory(category);
    }

    @Transactional
    public boolean setPlatform(int game_id, int platform_id) {
        if(game_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        if(platform_id <= 0){
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform ID must be greater than 0");
        }

        Game game = findGameById(game_id);
        if(game == null){
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
        }
        Platform platform = platformRepository.findById(platform_id);
        if(platform == null){
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Platform with ID %d does not exist", platform_id));
        }
        if (game.getPlatforms().contains(platform)) {
            return game.removePlatform(platform);
        }
        return game.addPlatform(platform);
    }
}
