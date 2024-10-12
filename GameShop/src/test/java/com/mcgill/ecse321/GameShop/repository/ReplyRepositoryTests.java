package com.mcgill.ecse321.GameShop.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.valueOf;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Category;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
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
    private AccountRepository accountRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        replyRepository.deleteAll();
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        accountRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    public void testCreateAndReadReply() {
       
        String email = "tarek.elakkaoui@hotmail.com";
        String username = "TarekElAkkaoui";
        String password = "pass";
        int phoneNumber = 0110;
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
        String reviewDescription = "This is a good game";

        String customerEmail = "anthony.saber@hotmail.lbbb";
        String customerUsername = "AnthonySaberrr";
        String customerPassword = "pas";
        int customerPhoneNumber = 1111;
        String customerAddress = "1234 rue Mtl";

        Date replyDate = Date.valueOf("2024-10-10");
        String replyDescription = "Thank you";

        Cart cart = new Cart();
        cart = cartRepository.save(cart);

        Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerPhoneNumber, customerAddress, cart);
        customer = accountRepository.save(customer);
    
        Manager createdManager = new Manager(email, username, password, phoneNumber, address);
        createdManager = accountRepository.save(createdManager);
        
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
        assertEquals(reply.getReviewRating(), pulledReply.getReviewRating());
        }       
}







