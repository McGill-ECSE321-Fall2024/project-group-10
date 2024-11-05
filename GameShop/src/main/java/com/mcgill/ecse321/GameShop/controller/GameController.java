package com.mcgill.ecse321.GameShop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mcgill.ecse321.GameShop.dto.GameDto.GameListDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameRequestDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameResponseDto;
import com.mcgill.ecse321.GameShop.dto.GameDto.GameSummaryDto;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.service.GameService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class GameController { // TODO, should we implement Delete for this controller?
    
    @Autowired
    private GameService gameService;

    @GetMapping("/games/{game_id}")
    public GameResponseDto findGameById(@PathVariable int game_id) {
        Game game = gameService.findGameById(game_id);
        return GameResponseDto.create(game);
    }

    @GetMapping("/games")
    public GameListDto findAllGames() {
        List<GameSummaryDto> dtos = new ArrayList<GameSummaryDto>();

        for (Game game : gameService.getAllGames()) {
            dtos.add(new GameSummaryDto(game));
        }

        return new GameListDto(dtos);
    }

    @PostMapping("/games")
    public GameResponseDto createGame(@Valid @RequestBody GameRequestDto request) {
        Game createdGame = gameService.CreateGame(request.getaTitle(), request.getaDescription(), request.getaPrice(), request.getaGameStatus(), request.getaStockQuantity(), request.getaPhotoUrl());
        return GameResponseDto.create(createdGame);
    }


    // TODO still have to implement PUT mapping for update

    // Archive Game (Upon archiving the app should require the owner to specify if the archived game should still appear in the catalogue), 
    
    // GetAllSpecificGames (return list of reg objects), GetGames (return list of reg objects) Add Game to platform

    // GetGamesByTitle, GetGamesByCategory, GetGameByPlatform, GetGameByStatus

    // Add/Remove game from platform/category

    // UpdateAllFields

    // AddSpecificGames.
    
}
