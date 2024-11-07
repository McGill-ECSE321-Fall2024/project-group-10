package com.mcgill.ecse321.GameShop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.dto.CategoryDto.CategoryResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.dto.PlatformDto.PlatformRequestDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameListDto;
import com.mcgill.ecse321.GameShop.dto.SpecificGameDto.SpecificGameSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Platform;
import com.mcgill.ecse321.GameShop.service.CategoryService;
import com.mcgill.ecse321.GameShop.service.GameService;
import com.mcgill.ecse321.GameShop.service.PlatformService;
import com.mcgill.ecse321.GameShop.service.SpecificGameService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class GameController { // TODO still have to take into account inventory
    
    @Autowired
    private GameService gameService;
    @Autowired
    private PlatformService PlatformService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpecificGameService specificGameService;

    @GetMapping("/games/{game_id}")
    public GameResponseDto findGameById(@PathVariable int game_id) {
        Game game = gameService.findGameById(game_id);
        return GameResponseDto.create(game);
    }

    @GetMapping("/games")
    public GameListDto findAllGames() { // TODO needs to be checked for fixes
        List<GameSummaryDto> dtos = new ArrayList<GameSummaryDto>();

        for (Game game : gameService.getAllGames()) {
            dtos.add(new GameSummaryDto(game));
        }

        return new GameListDto(dtos);
    }

    @PostMapping("/games")
    public GameResponseDto createGame(@Valid @RequestBody GameRequestDto request) {
        Game createdGame = gameService.CreateGame(request.getaTitle(), request.getaDescription(), request.getaPrice(), request.getaGameStatus(), request.getaStockQuantity(), request.getaPhotoUrl());

        if (request.getCategories() != null) {
        
            gameService.updateCategories(createdGame, request.getCategories());
      
        
        }
        if (request.getPlatforms() != null) {
            gameService.updatePlatforms(createdGame, request.getPlatforms());
        }
  
        return GameResponseDto.create(createdGame);
    }

    @DeleteMapping("/games/{game_id}") // TODO, no soft delete for now, has to be implemented
    // Upon archiving the app should require the owner to specify if the archived game should still appear in the catalogue
    public void deleteGame(@PathVariable int game_id) {
        gameService.deleteGame(game_id);
    }

    @PutMapping("/games/{id}")
    public Game putMethod(@PathVariable int id, @RequestBody String request) { // TODO needs to be checked for fixes
        Game game = gameService.findGameById(id);

        game.setTitle(game.getTitle());
        game.setDescription(game.getDescription());
        game.setPrice(game.getPrice());
        game.setGameStatus(game.getGameStatus());
        game.setStockQuantity(game.getStockQuantity());
        game.setPhotoUrl(game.getPhotoUrl());

        return game;
    }
    // @PutMapping("/games/categories/{game_id}")
    // public GameResponseDto updateListOfCategories(@PathVariable int id,@RequestBody GameRequestDto request) {
    //     Game game = gameService.updateCategories(id, request.getCategories());
    //     return GameResponseDto.create(game);
    // }

    @PutMapping("/games/platform/{game_id}/{platform_id}")
    public Boolean setPlatform(@PathVariable int game_id, @PathVariable int platform_id) {
        return gameService.setPlatform(game_id, platform_id);
    }

    @PutMapping("/games/category/{game_id}/{category_id}") 
    public Boolean setCategory(@PathVariable int game_id, @PathVariable int category_id) {
        return gameService.setCategory(game_id, category_id);
    }
    
    @GetMapping("/games/Title/{Title}")
    public Iterable<Game> getGamesByTitle(@RequestParam String Title) {
        Iterable<Game> games = gameService.getGamesByTitle(Title);
        return games;
    }

    @GetMapping("/games/Category/{category_id}")
    public Iterable<Game> getGamesByCategory(@RequestParam int category_id) {
        Category category = categoryService.getCategory(category_id);
        Iterable<Game> games = gameService.getGamesByCategory(category);
        return games;
    }

    @GetMapping("/games/Platform/{platform_id}")
    public Iterable<Game> getGamesByPlatform(@RequestParam int platform_id) {
        Platform platform = PlatformService.getPlatform(platform_id);
        Iterable<Game> games = gameService.getGamesByPlatform(platform);
        return games;
    }

    @GetMapping("/games/Status/{status}")
    public Iterable<Game> getGamesByStatus(@RequestParam GameStatus status) {
        Iterable<Game> games = gameService.getGamesByStatus(status);
        return games;
    }

    @PostMapping("/games/specificGame/{game_id}")
    public void postMethodName(@RequestParam int game_id, @RequestParam int numberOfCopies) {
        Game game = gameService.findGameById(game_id);
        for (int i = 0; i < numberOfCopies; i++) {
            specificGameService.createSpecificGame(game);
        }
    }
    
    
}
