package com.mcgill.ecse321.GameShop.repository;

import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.SpecificGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;


@SpringBootTest
public class SpecificGameRepositoryTests {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private SpecificGameRepository specificGameRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        categoryRepository.deleteAll();
        specificGameRepository.deleteAll();
        gameRepository.deleteAll();
       
    }

    @Test
    public void TestCreateAndReadSpecificGame(){
        //create first game
        String title = "game1";
        String description = "game1_description";
        int price = 25;
        GameStatus gameStatus = GameStatus.OutOfStock;
        int stockQuantity = 50;
        String photoUrl = "wwww.photo1.com";

        Game game = new Game(title, description, price, gameStatus, stockQuantity, photoUrl);
        game = gameRepository.save(game);
        int gameId = game.getGame_id();

        SpecificGame specificGame = new SpecificGame(game);
        specificGame = specificGameRepository.save(specificGame);
        
        SpecificGame specificGameFromDb = specificGameRepository.findById(specificGame.getSpecificGame_id());

        // SpecificGame gameFromDb = specificGameRepository.findById(gameId);


        
    }
    
}
