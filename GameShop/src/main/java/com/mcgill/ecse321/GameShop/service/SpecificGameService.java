package com.mcgill.ecse321.GameShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import com.mcgill.ecse321.GameShop.repository.GameRepository;
import com.mcgill.ecse321.GameShop.repository.SpecificGameRepository;

@Service
public class SpecificGameService {

    @Autowired
    private SpecificGameRepository specificGameRepository;

    public SpecificGame createSpecificGame(Game game) {
        SpecificGame specificGame = new SpecificGame(game);
        return specificGame;
    }

    public SpecificGame findSpecificGameById(int specificGame_id) {
        SpecificGame specificGame = specificGameRepository.findById(specificGame_id);
        if (specificGame == null) {
            throw new IllegalArgumentException("There is no SpecificGame with ID " + specificGame_id + ".");
        }
        return specificGame;
    }

    public Iterable<SpecificGame> getAllSpecificGames() {
        return specificGameRepository.findAll();
    }

    public SpecificGame updateSpecificGameItemStatus(int specificGame_id, SpecificGame.ItemStatus newItemStatus) {
        SpecificGame specificGame = findSpecificGameById(specificGame_id);
        specificGame.setItemStatus(newItemStatus);
        specificGameRepository.save(specificGame);
        return specificGame;
    }

}