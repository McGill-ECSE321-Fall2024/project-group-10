package com.mcgill.ecse321.GameShop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.SpecificGameRepository;

import jakarta.transaction.Transactional;

@Service
public class SpecificGameService {

    @Autowired
    private SpecificGameRepository specificGameRepository;
    
    @Autowired
    private GameService gameService;
    @Autowired
    private GameRepository gameRepository;

    @Transactional
    public SpecificGame createSpecificGame(Game game) {
        if(game == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game cannot be null");
        }
        SpecificGame specificGame = new SpecificGame(game);
        specificGameRepository.save(specificGame);
        return specificGame;
    }

    @Transactional
    public SpecificGame findSpecificGameById(int specificGame_id) {
        if (specificGame_id <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "SpecificGame ID must be greater than 0");
        }
        SpecificGame specificGame = specificGameRepository.findById(specificGame_id);
        if (specificGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("SpecificGame does not exist"));
        }
        return specificGame;
    }

    @Transactional
    public SpecificGame updateSpecificGame(int specificGame_id, int game_id) {
        if (specificGame_id <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "SpecificGame ID must be greater than 0");
        }
        if (game_id <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        SpecificGame specificGame = findSpecificGameById(specificGame_id);
        if(specificGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "SpecificGame does not exist");
        }
        Game game = gameService.findGameById(game_id);
        if(game == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game does not exist");
        }
        specificGame.setGames(game);
        specificGameRepository.save(specificGame);
        return specificGame;
    }

    @Transactional
    public Iterable<SpecificGame> getSpecificGamesByGameId(int game_id) {
        if (game_id <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Game ID must be greater than 0");
        }
        if (gameRepository.findById(game_id) == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Game does not exist");
        }
    
    Iterable<SpecificGame> specificGames = this.getAllSpecificGames();
    if (specificGames == null) {
        throw new GameShopException(HttpStatus.NOT_FOUND, "No SpecificGame found at all");
        
    }
    List<SpecificGame> specificGameList = new ArrayList<>();

    for (SpecificGame specificGame : specificGames) {
        if (specificGame.getGames().getGame_id() == game_id) {
            specificGameList.add(specificGame);
        }
    }

    // Throw an exception only if no specific games match the given game_id
    if (specificGameList.isEmpty()) {
        throw new GameShopException(HttpStatus.NOT_FOUND, "No SpecificGame found for this Game ID");
    }

    return specificGameList;
}
    

    @Transactional
    public Iterable<SpecificGame> getAllSpecificGames() {
        Iterable<SpecificGame> specificGames=specificGameRepository.findAll();
        if (!specificGames.iterator().hasNext()) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "No SpecificGame found at all");
        }
        return specificGames;
    }

    @Transactional
    public SpecificGame updateSpecificGameItemStatus(int specificGame_id, SpecificGame.ItemStatus newItemStatus) {
        if (specificGame_id <= 0) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "SpecificGame ID must be greater than 0");
        }
        if(newItemStatus == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "ItemStatus cannot be null");
        }
        SpecificGame specificGame = findSpecificGameById(specificGame_id);

        if (specificGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "SpecificGame does not exist");
        }

        specificGame.setItemStatus(newItemStatus);
        specificGameRepository.save(specificGame);
        return specificGame;
    }

}