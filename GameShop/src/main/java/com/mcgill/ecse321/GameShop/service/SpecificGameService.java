package com.mcgill.ecse321.GameShop.service;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.repository.SpecificGameRepository;

import jakarta.transaction.Transactional;

@Service
public class SpecificGameService {

    @Autowired
    private SpecificGameRepository specificGameRepository;
    
    @Autowired
    private GameService gameService;

    @Transactional
    public SpecificGame createSpecificGame(Game game) {
        SpecificGame specificGame = new SpecificGame(game);
        specificGameRepository.save(specificGame);
        return specificGame;
    }

    @Transactional
    public SpecificGame findSpecificGameById(int specificGame_id) {
        SpecificGame specificGame = specificGameRepository.findById(specificGame_id);
        if (specificGame == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("SpecificGame does not exist"));
        }
        return specificGame;
    }

    @Transactional
    public void updateSpecficGame(int specificGame_id, int game_id) {
        SpecificGame specificGame = findSpecificGameById(specificGame_id);
        Game game = gameService.findGameById(game_id);
        specificGame.setGames(game);
        specificGameRepository.save(specificGame);
    }

    @Transactional
    public Iterable<SpecificGame> getSpecificGamesByGameId(int game_id) {
        Iterable<SpecificGame> specificGames = this.getAllSpecificGames();
        List<SpecificGame> specificGameList = new ArrayList<SpecificGame>();

        for (SpecificGame specificGame : specificGames) {
            if (specificGame.getGames().getGame_id() == game_id) {
                specificGameList.add(specificGame);
            }
        }

        return specificGameList;
    }

    @Transactional
    public Iterable<SpecificGame> getAllSpecificGames() {
        return specificGameRepository.findAll();
    }

    @Transactional
    public SpecificGame updateSpecificGameItemStatus(int specificGame_id, SpecificGame.ItemStatus newItemStatus) {
        SpecificGame specificGame = findSpecificGameById(specificGame_id);
        if (newItemStatus == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "ItemStatus cannot be null");
        }
        specificGame.setItemStatus(newItemStatus);
        specificGameRepository.save(specificGame);
        return specificGame;
    }

}