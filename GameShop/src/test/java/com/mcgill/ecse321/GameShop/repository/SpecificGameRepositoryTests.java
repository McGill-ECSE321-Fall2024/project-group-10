package com.mcgill.ecse321.GameShop.repository;

import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.SpecificGame.ItemStatus;
import com.mcgill.ecse321.GameShop.model.Order;
import com.mcgill.ecse321.GameShop.model.SpecificGame;
import java.sql.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        specificGameRepository.deleteAll();
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
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
        ItemStatus itemStatus = ItemStatus.Confirmed;

        Game game = new Game(title, description, price, gameStatus, stockQuantity, photoUrl);
        game = gameRepository.save(game);
        int gameId = game.getGame_id();

        SpecificGame specificGame = new SpecificGame(game);
        specificGame.setItemStatus(itemStatus);
        specificGame = specificGameRepository.save(specificGame);
        
        // SpecificGame specificGameFromDb = specificGameRepository.findById(specificGame.getSpecificGame_id());

        // SpecificGame gameFromDb = specificGameRepository.findById(gameId);

        String email = "judes10@gmail.com";
        String username = "JudeSousou1";
        String password = "p1234";
        String phoneNumber = "+1 (438) 320-239";
        String address = "1110 rue Sainte-Catherine";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        int cartId = cart.getCart_id();

        Customer customer1 = new Customer(email, username, password, phoneNumber, address, cart);
        customer1 = customerRepository.save(customer1);

        Date orderDate = Date.valueOf("2021-10-10");

        String note = "testing";
        int paymentCard = 1209028310;

        Order order = new Order(orderDate, note, paymentCard, customer1);
        order = orderRepository.save(order);

        String trackingNumber = order.getTrackingNumber();
        

        Order orderFromDb = orderRepository.findByTrackingNumber(trackingNumber);
        specificGame.addOrder(orderFromDb);

        specificGame = specificGameRepository.save(specificGame);

        SpecificGame specificGameFromDb = specificGameRepository.findById(specificGame.getSpecificGame_id());
        
        assertNotNull(specificGameFromDb);
        assertEquals(itemStatus, specificGameFromDb.getItemStatus());
        
    }
    
}
