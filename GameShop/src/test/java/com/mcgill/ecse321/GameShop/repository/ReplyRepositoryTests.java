package com.mcgill.ecse321.GameShop.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.model.Reply;
import com.mcgill.ecse321.GameShop.model.Game.GameStatus;
import com.mcgill.ecse321.GameShop.model.Review.GameRating;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        replyRepository.deleteAll();
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        managerRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadReply() {
       
        String email = "tarek.elakkaoui@gmail.com";
        String username = "TarekElAkkaoui";
        String password = "pass";
        String phoneNumber = "+1 (438) 865-9298";
        String address = "0000 rue Liban";

        String title = "God of War";
        String gameDescription = "A father and son duo";
        GameStatus status = GameStatus.InStock;
        int stock = 10;
        String url = "www.";
        int price = 61;

        Date reviewDate = Date.valueOf("2024-10-09");
        int rating = 0;
        GameRating gameRating = GameRating.One;
        String reviewDescription = "This is a bad game";

        String customerEmail = "anthony.saber@hotmail.lbbb";
        String customerUsername = "AnthonySaberrr";
        String customerPassword = "pas";
        String customerPhoneNumber = "+1 (438) 865-9298";
        String customerAddress = "1234 rue Mtl";

        Date replyDate = Date.valueOf("2024-10-10");
        String replyDescription = "Thank you";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerPhoneNumber, customerAddress, cart);
        customer = customerRepository.save(customer);
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = managerRepository.save(createdManager);
        
        Game game = new Game(title, gameDescription, price, status, stock, url);
        game = gameRepository.save(game);

        Review review = new Review(reviewDate, reviewDescription, rating, gameRating, game, customer);
        review = reviewRepository.save(review);

        Reply reply = new Reply(replyDate, replyDescription, review, createdManager);
        reply.setReviewRating(Reply.ReviewRating.Like);
        reply = replyRepository.save(reply);
        int replyId = reply.getReply_id();

        Reply pulledReply = replyRepository.findById(replyId);

        // Assertions
        assertNotNull(pulledReply);
        assertEquals(reply.getDescription(), pulledReply.getDescription());
        assertEquals(reply.getReplyDate(), pulledReply.getReplyDate());
        assertEquals(reply.getReviewRating(), pulledReply.getReviewRating());
        assertEquals(reply.getReview().getReview_id(), pulledReply.getReview().getReview_id());
        assertEquals(createdManager.getEmail(), reply.getManager().getEmail());
        assertTrue(pulledReply.getManager() instanceof Manager, "The reply account should be a manager.");
        }       
}








