package com.mcgill.ecse321.GameShop.service;

import java.util.ArrayList;
import java.beans.Transient;
import java.util.List;
import java.util.Map;

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
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
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
    public Iterable<Game> getGamesByStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative");
        }
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByStockQuantity = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getStockQuantity() == stockQuantity) {
                gamesByStockQuantity.add(game);
            }
        }
        if(gamesByStockQuantity.isEmpty()){
            throw new GameShopException(HttpStatus.NOT_FOUND, "No games with the given stock quantity");
        }
        return gamesByStockQuantity;
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
                // game.getPlatformSpecificMap().put(platform, new ArrayList<>());
                // game.getHistoryMap().put(platform, new ArrayList<>());
            }

        }
        gameRepository.save(game);
        return game;
    }
    
    @Transactional 
    public Game deleteGame(int game_id) {
        Game game = findGameById(game_id);
        game.setGameStatus(GameStatus.Archived);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game addCategory(int game_id, int category_id) {
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
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Category is already in the game");
        }
         game.addCategory(category);
         gameRepository.save(game);
         return game;
    }

    @Transactional
    public Game addPlatform(int game_id, int platform_id) {
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
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform is already in the game");
        }
        game.addPlatform(platform);
        game.getPlatformSpecificMap().put(platform, new ArrayList<>());
        game.getHistoryMap().put(platform, new ArrayList<>());
        gameRepository.save(game);
        return game;
    }

    // @Transactional
    // public List<Integer> getSpecificGamesByPlatform(int game_id, int platform_id) {
    //     if(game_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
    //     }
    //     if (platform_id <= 0) {
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform ID must be greater than 0");
    //     }
    //     Game game = findGameById(game_id);
    //     if(game == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
    //     }
    //     Platform platform = platformRepository.findById(platform_id);
    //     if(platform == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Platform with ID %d does not exist", platform_id));
    //     }
    //     if(!game.getPlatforms().contains(platform)){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, "Platform is not in the game");
    //     }
    //     return game.getPlatformSpecificMap().get(platform);
    // }

    // @Transactional
    // public Platform findPlatformByGameHistory(int game_id, int specific_game_id){
    //     if(game_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
    //     }
    //     if(specific_game_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific Game ID must be greater than 0");
    //     }
    //     Game game = findGameById(game_id);
    //     if(game == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
    //     }

    //     Map<Platform, List<Integer>> historyMap = game.getHistoryMap();

    //     for( Map.Entry<Platform, List<Integer>> entry : historyMap.entrySet() ){
    //         if(entry.getValue().contains(specific_game_id)){
    //             return entry.getKey();
    //         }
    //     }

    //     throw new GameShopException(HttpStatus.NOT_FOUND, "Specific game does not exist in the game");
    // }

    // @Transactional
    // public void addSpecificGameToSpecificGamesByPlatform(int game_id, int platform_id, int specificGame_id) {
    //     if(game_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
    //     }
    //     if(platform_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform ID must be greater than 0");
    //     }
    //     if(specificGame_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific Game ID must be greater than 0");
    //     }
    //     Game game = findGameById(game_id);
    //     if(game == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
    //     }
    //     Platform platform = platformRepository.findById(platform_id);
    //     if(platform == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Platform with ID %d does not exist", platform_id));
    //     }
    //     if(!game.getPlatforms().contains(platform)){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, "Platform is not in the game");
    //     }
    //     if (!game.addToPlatformSpecificMap(platform, specificGame_id)) {
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific game already exists in the platform");
    //     }
    // }

    // @Transactional
    // public void addSpecificGameToHistoryMap(int game_id, int specificGame_id) {
    //     if(game_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
    //     }
    //     if(specificGame_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific Game ID must be greater than 0");
    //     }
    //     Game game = findGameById(game_id);
    //     if(game == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
    //     }
    //     Platform platform = findPlatformByGameHistory(game_id, specificGame_id);
    //     if(platform == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, "Platform does not exist");
    //     }
    //     if (!game.addToHistoryMap(platform, specificGame_id)) {
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific game already exists in the history map");
    //     }
    // }

    // @Transactional
    // public void removeSpecificGameFromSpecificGamesByPlatform(int game_id, int platform_id, int specificGame_id) {
    //     if(game_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
    //     }
    //     if(platform_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Platform ID must be greater than 0");
    //     }
    //     if(specificGame_id <= 0){
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific Game ID must be greater than 0");
    //     }
    //     Game game = findGameById(game_id);
    //     if(game == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with ID %d does not exist", game_id));
    //     }
    //     Platform platform = platformRepository.findById(platform_id);
    //     if(platform == null){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Platform with ID %d does not exist", platform_id));
    //     }
    //     if(!game.getPlatforms().contains(platform)){
    //         throw new GameShopException(HttpStatus.NOT_FOUND, "Platform is not in the game");
    //     }
    //     if (!game.removeFromPlatformSpecificMap(platform, specificGame_id)) {
    //         throw new GameShopException(HttpStatus.BAD_REQUEST, "Specific game does not exist in the platform");
    //     }
    // }
}
