package com.mcgill.ecse321.GameShop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

import jakarta.transaction.Transactional;

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
    private EmployeeRepository employeeRepository;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        replyRepository.deleteAll();
        reviewRepository.deleteAll();
        gameRepository.deleteAll();
        employeeRepository.deleteAll();
        managerRepository.deleteAll();
        customerRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateAndReadReply() {
        // Create necessary objects
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

        Customer customer = new Customer(customerEmail, customerUsername, customerPassword, customerPhoneNumber,
                customerAddress, cart);
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

    @Test
    @Transactional
    public void testCreateMultipleRepliesToReview() {
        // Create necessary objects
        Cart cart2 = new Cart();
        cart2 = cartRepository.save(cart2);

        Customer customer = new Customer("101@101.com", "101", "password",
                "+1 (101) 456-7890", "101 Rue Egypt", cart2);
        customer = customerRepository.save(customer);

        Manager manager = new Manager("102@102.com", "102User", "pas",
                "+1 (102) 654-3210", "102 Rue Liban");
        manager = managerRepository.save(manager);

        Game game = new Game("Batman", "I am Batman", 59, GameStatus.InStock, 101,
                "batmanlink.com");
        game = gameRepository.save(game);

        Review review = new Review(Date.valueOf("2024-01-10"), "I Love this Game!", 5,
                GameRating.Five, game, customer);
        review = reviewRepository.save(review);

        // Create first reply
        Reply firstReply = new Reply(Date.valueOf("2023-10-02"), "Thank you for your feedback!!!",
                review, manager);
        firstReply.setReviewRating(Reply.ReviewRating.Like);
        firstReply = replyRepository.save(firstReply);

        // Create second reply
        Reply secondReply = new Reply(Date.valueOf("2023-10-03"), "We appreciate your support!!!!",
                review, manager);
        secondReply.setReviewRating(Reply.ReviewRating.Like);
        secondReply = replyRepository.save(secondReply);

        // Retrieve all replies
        Iterable<Reply> allReplies = replyRepository.findAll();

        // Filter replies associated with the specific review
        List<Reply> repliesForReview = new ArrayList<>();
        for (Reply reply : allReplies) {
            if (reply.getReview().equals(review) && reply.getReview().getReview_id() == review.getReview_id()) {
                repliesForReview.add(reply);
            }
        }

        // Assertions
        assertNotNull(repliesForReview, "Replies list should not be null.");
        assertEquals(2, repliesForReview.size(), "There should be 2 replies associated with the review.");

        // Verify that both replies are present in the retrieved list
        assertTrue(repliesForReview.contains(firstReply), "First reply should be associated with the review.");
        assertTrue(repliesForReview.contains(secondReply), "Second reply should be associated with the review.");
    }

    @Test
    @Transactional
    public void testCreateReplyWithoutManager() {
        // Create necessary objects
        Cart cart3 = new Cart();
        cart3 = cartRepository.save(cart3);

        Customer customer = new Customer("103@103.com", "103", "password", "+1 (103) 456-7892", "103 rue Mtl", cart3);
        customer = customerRepository.save(customer);

        Game game = new Game("Batman 3", "I am batman 3", 30, GameStatus.InStock, 30, "www.apple.com");
        game = gameRepository.save(game);

        Review review = new Review(Date.valueOf("2022-10-20"), "Could be better.", 0, GameRating.Three, game, customer);
        review = reviewRepository.save(review);

        // Attempt to create a reply without a manager
        Exception exception = null;
        try {
            Reply replyWithoutManager = new Reply(Date.valueOf("2023-10-21"), "Please provide more details.", review,
                    null);
            replyWithoutManager.setReviewRating(Reply.ReviewRating.Dislike);
            replyRepository.save(replyWithoutManager);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception, "Reply should be done by an existing manager");
    }

    @Test
    @Transactional
    public void testCreateReplyWithoutReview() {
        // Create necessary objects
        Cart cart4 = new Cart();
        cart4 = cartRepository.save(cart4);

        Customer customer = new Customer("102100@102.com", "102", "password", "+1 (000) 456-7892", "102 Rue Liban",
                cart4);
        customer = customerRepository.save(customer);

        Game game = new Game("Batman 2", "I am batman 2", 20, GameStatus.InStock, 20, "randomlink.com");
        game = gameRepository.save(game);

        Manager manager = new Manager("102101032@102.com", "102103102", "pa", "+1 (102101032) 456-7892",
                "102101032 Rue Canada");
        manager = managerRepository.save(manager);

        // Attempt to create a reply without a review
        Exception exception = null;
        try {
            Reply replyWithoutManager = new Reply(Date.valueOf("2023-10-15"), "Please provide details!!!", null,
                    manager);
            replyWithoutManager.setReviewRating(Reply.ReviewRating.Dislike);
            replyRepository.save(replyWithoutManager);
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception, "Reply should be done to a review.");
    }

}