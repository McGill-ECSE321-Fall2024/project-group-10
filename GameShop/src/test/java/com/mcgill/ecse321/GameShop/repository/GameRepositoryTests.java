// package com.mcgill.ecse321.GameShop.repository;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import com.mcgill.ecse321.GameShop.model.Game;
// import com.mcgill.ecse321.GameShop.model.Game.GameStatus;

// @SpringBootTest
// public class GameRepositoryTests {
    
//     @Autowired
//     private GameRepository gameRepository;
//     @Autowired
//     private SpecificGameRepository specificGameRepository;
//     @Autowired
//     private CategoryRepository categoryRepository;

//     @BeforeEach
//     @AfterEach
//     public void clearDatabase() {
//         categoryRepository.deleteAll();
//         specificGameRepository.deleteAll();
//         gameRepository.deleteAll();
       
//     }

//     @Test
//     public void testCreateAndReadGame(){
//         String title = "Super Mario";
//         String description = "Mario";
//         int price = 25;
//         GameStatus gameStatus = GameStatus.InStock;
//         int stockQuantity = 55;
//         String photoUrl = "wwww.photo1.com";

//         Game game = new Game(title, description, price, gameStatus, stockQuantity, photoUrl);
//         game = gameRepository.save(game);

        


//     }

    
// }
