package com.mcgill.ecse321.GameShop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.repository.GameRepository;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PlatformService platformService;

    public boolean isEmpty(String str){
        return str == null || str.trim().isEmpty();
    }
    
    @Transactional
    public Game CreateGame(String title, String aDescription, int aPrice, Game.GameStatus aGameStatus, int aStockQuantity, String aPhotoUrl) {
        if (isEmpty(aPhotoUrl) || isEmpty(title) || isEmpty(aDescription) || aPrice < 0 || aStockQuantity < 0 || aGameStatus == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Constructor fields for Game cannot be empty, null, or negative");
        }
        Game game = new Game(title, aDescription, aPrice, aGameStatus, aStockQuantity, aPhotoUrl);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game findGameById(int game_id) { // TODO should we validate the game id > 0?
        Game game = gameRepository.findById(game_id); // check diff with findbyPersonId
        if (game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game does not exist"));
        }
        return game;
    }

    @Transactional
    public Iterable<Game> getGamesByTitle(String title) {
        Iterable<Game> games = this.getAllGames();
        for (Game game : games) {
            if (game.getTitle().equals(title)) {
                return games;
            }
        }
        throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Game with title %s does not exist", title));
    }

    @Transactional 
    public Iterable<Game> getGamesByCategory(Category category){
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByCategory = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getCategories().contains(category)) {
                gamesByCategory.add(game);
            }
        }
        return gamesByCategory;
    }

    @Transactional
    public Iterable<Game> getGamesByPlatform(Platform platform) {
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByPlatform = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getPlatforms().contains(platform)) {
                gamesByPlatform.add(game);
            }
        }
        return gamesByPlatform;
    }

    @Transactional
    public Iterable<Game> getGamesByStatus(GameStatus status){
        Iterable<Game> games = this.getAllGames();
        List<Game> gamesByStatus = new ArrayList<Game>();
        for (Game game : games) {
            if (game.getGameStatus().equals(status)) {
                gamesByStatus.add(game);
            }
        }
        return gamesByStatus;
    }
    

    @Transactional
    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Transactional
    public Game updateGameDescription(int game_id, String newDescription) {
        Game game = findGameById(game_id);
        if (isEmpty(newDescription)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Description cannot be empty or null");
        }
        game.setDescription(newDescription);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGamePrice(int game_id, int newPrice) {
        Game game = findGameById(game_id);
        if (newPrice < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Price cannot be negative");
        }
        game.setPrice(newPrice);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGameStockQuantity(int game_id, int newStockQuantity) {
        Game game = findGameById(game_id);
        if (newStockQuantity < 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Stock quantity cannot be negative");
        }
        game.setStockQuantity(newStockQuantity);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGameStatus(int game_id, Game.GameStatus newGameStatus) {
        Game game = findGameById(game_id);
        if (newGameStatus == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game status cannot be null");
        }
        game.setGameStatus(newGameStatus);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGamePhotoUrl(int game_id, String newPhotoUrl) {
        Game game = findGameById(game_id);
        if (isEmpty(newPhotoUrl)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Photo URL cannot be empty or null");
        }
        game.setPhotoUrl(newPhotoUrl);
        gameRepository.save(game);
        return game;
    }

    @Transactional
    public Game updateGameTitle(int game_id, String newTitle) {
        Game game = findGameById(game_id);
        if (isEmpty(newTitle)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Title cannot be empty or null");
        }
        game.setTitle(newTitle);
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
        Game game = findGameById(game_id);
        Category category = categoryService.getCategory(category_id);
        if (game.getCategories().contains(category)) {
            return game.removeCategory(category);
        }
        return game.addCategory(category);
    }

    @Transactional
    public boolean setPlatform(int game_id, int platform_id) {
        Game game = findGameById(game_id);
        Platform platform = platformService.getPlatform(platform_id);
        if (game.getPlatforms().contains(platform)) {
            return game.removePlatform(platform);
        }
        return game.addPlatform(platform);
    }
}
