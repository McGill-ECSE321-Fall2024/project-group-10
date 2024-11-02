package com.mcgill.ecse321.GameShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.repository.GameRepository;

@Service
public class GameService {
    
    @Autowired
    private GameRepository gameRepository;

    public Game CreateGame(String title, String aDescription, int aPrice, Game.GameStatus aGameStatus, int aStockQuantity, String aPhotoUrl) {
        Game game = new Game(title, aDescription, aPrice, aGameStatus, aStockQuantity, aPhotoUrl);
        return game;
    }

    public Game findGameById(int game_id) {
        Game game = gameRepository.findById(game_id); // check diff with findbyPersonId
        if (game == null) {
            throw new IllegalArgumentException("There is no Game with ID" + game_id + ".");
        }
        return game;
    }

    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game updateGameDescription(int game_id, String newDescription) {
        Game game = findGameById(game_id);
        game.setDescription(newDescription);
        gameRepository.save(game);
        return game;
    }

    public Game updateGamePrice(int game_id, int newPrice) {
        Game game = findGameById(game_id);
        game.setPrice(newPrice);
        gameRepository.save(game);
        return game;
    }

    public Game updateGameStockQuantity(int game_id, int newStockQuantity) {
        Game game = findGameById(game_id);
        game.setStockQuantity(newStockQuantity);
        gameRepository.save(game);
        return game;
    }

    public Game updateGameStatus(int game_id, Game.GameStatus newGameStatus) {
        Game game = findGameById(game_id);
        game.setGameStatus(newGameStatus);
        gameRepository.save(game);
        return game;
    }

    public Game updateGamePhotoUrl(int game_id, String newPhotoUrl) {
        Game game = findGameById(game_id);
        game.setPhotoUrl(newPhotoUrl);
        gameRepository.save(game);
        return game;
    }

    public Game updateGameTitle(int game_id, String newTitle) {
        Game game = findGameById(game_id);
        game.setTitle(newTitle);
        gameRepository.save(game);
        return game;
    }
    
}
