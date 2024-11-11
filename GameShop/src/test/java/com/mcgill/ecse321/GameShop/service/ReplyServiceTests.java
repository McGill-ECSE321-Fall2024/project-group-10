package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Game;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Reply;
import com.mcgill.ecse321.GameShop.model.Reply.ReviewRating;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.ReplyRepository;
import com.mcgill.ecse321.GameShop.repository.ReviewRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ReplyServiceTests {

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReplyService replyService;

    // --- Tests for createReply ---

    @Test
    public void testCreateValidReply() {
        // Arrange
        int replyId = 1001;
        int reviewId = 2001;
        int gameId = 3001; 
        String customerEmail = "customer1001@example.com";
        String managerEmail = "manager1034879@example.com"; 
        Date replyDate = Date.valueOf("2023-10-10");
        String description = "Thank you for your feedback!";
        ReviewRating reviewRating = ReviewRating.Like;

        // Create Cart
        Cart cart = new Cart();

        // Create Game
        Game game = new Game("Test Game", "A test game", 50, Game.GameStatus.InStock, 100, "testPhotoUrl");
        game.setGame_id(gameId);

        // Create Customer
        Customer customer = new Customer(customerEmail, "customerUser1", "customerPass1", "123-456-7890", "456 Customer Street", cart);

        // Create Manager
        Manager manager = new Manager(managerEmail, "managerUser1", "managerPass1", "123-456-7890", "123 Manager Street");

        // Create Review with Game and Customer
        Review review = new Review(LocalDate.parse("2023-10-01"), "Great game!", 5, Review.GameRating.Five, game, customer);
        review.setReview_id(reviewId);

        // Mock repositories
        when(managerRepository.findByEmail(managerEmail)).thenReturn(manager);
        when(reviewRepository.findById(reviewId)).thenReturn(review);
        when(replyRepository.save(any(Reply.class))).thenAnswer((InvocationOnMock invocation) -> {
            Reply savedReply = invocation.getArgument(0);
            savedReply.setReply_id(replyId);
            return savedReply;
        });

        // Act
        Reply createdReply = replyService.createReply(
            replyDate,
            description,
            reviewRating,
            reviewId,
            managerEmail
        );

        // Assert
        assertNotNull(createdReply);
        assertEquals(replyId, createdReply.getReply_id());
        assertEquals(replyDate, createdReply.getReplyDate());
        assertEquals(description, createdReply.getDescription());
        assertEquals(reviewRating, createdReply.getReviewRating());
        assertEquals(review, createdReply.getReview());
        assertEquals(manager, createdReply.getManager());

        verify(managerRepository, times(1)).findByEmail(managerEmail);
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(replyRepository, times(1)).save(any(Reply.class));
    }


    @Test
    public void testCreateReplyWithNullDate() {
        // Arrange
        int reviewId = 2002;
        String managerEmail = "manager243874638@example.com";
        Date replyDate = null;
        String description = "We appreciate your feedback!";
        ReviewRating reviewRating = ReviewRating.Like;

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.createReply(
                replyDate,
                description,
                reviewRating,
                reviewId,
                managerEmail
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Reply date cannot be null", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(reviewRepository, never()).findById(anyInt());
        verify(replyRepository, never()).save(any(Reply.class));
    }

    @Test
    public void testCreateReplyWithNullDescription() {
        // Arrange
        int reviewId = 2003;
        String managerEmail = "manager293873@example.com";
        Date replyDate = Date.valueOf("2023-10-11");
        String description = null; // Null description
        ReviewRating reviewRating = ReviewRating.Like;

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.createReply(
                replyDate,
                description,
                reviewRating,
                reviewId,
                managerEmail
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Description cannot be empty or null", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(reviewRepository, never()).findById(anyInt());
        verify(replyRepository, never()).save(any(Reply.class));
    }

    @Test
    public void testCreateReplyWithNullReviewRating() {
        // Arrange
        int reviewId = 2004;
        String managerEmail = "manager44339476@example.com";
        Date replyDate = Date.valueOf("2023-10-12");
        String description = "Thank you!";
        ReviewRating reviewRating = null; // Null review rating

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.createReply(
                replyDate,
                description,
                reviewRating,
                reviewId,
                managerEmail
            );
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Review rating cannot be null", exception.getMessage());

        verify(managerRepository, never()).findByEmail(anyString());
        verify(reviewRepository, never()).findById(anyInt());
        verify(replyRepository, never()).save(any(Reply.class));
    }

    @Test
    public void testCreateReplyWithInvalidManagerEmail() {
        // Arrange
        int reviewId = 2005;
        String managerEmail = "invalidmanager473846@example.com"; // Invalid manager email
        Date replyDate = Date.valueOf("2023-10-13");
        String description = "Thanks for your review!";
        ReviewRating reviewRating = ReviewRating.Like;

        when(managerRepository.findByEmail(managerEmail)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.createReply(
                replyDate,
                description,
                reviewRating,
                reviewId,
                managerEmail
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Manager with email %s not found", managerEmail), exception.getMessage());

        verify(managerRepository, times(1)).findByEmail(managerEmail);
        verify(reviewRepository, never()).findById(anyInt());
        verify(replyRepository, never()).save(any(Reply.class));
    }

    @Test
    public void testCreateReplyWithInvalidReviewId() {
        // Arrange
        int reviewId = 2006; // Invalid review ID
        String managerEmail = "manager34348766@example.com"; // Unique manager email
        Date replyDate = Date.valueOf("2023-10-14");
        String description = "We're sorry to hear that.";
        ReviewRating reviewRating = ReviewRating.Dislike;

        Manager manager = new Manager(managerEmail, "managerUser6", "managerPass6", "123-456-7890", "123 Manager Street");
        when(managerRepository.findByEmail(managerEmail)).thenReturn(manager);
        when(reviewRepository.findById(reviewId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.createReply(
                replyDate,
                description,
                reviewRating,
                reviewId,
                managerEmail
            );
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(String.format("Review with ID %d not found", reviewId), exception.getMessage());

        verify(managerRepository, times(1)).findByEmail(managerEmail);
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(replyRepository, never()).save(any(Reply.class));
    }

    // --- Tests for getReplyById ---

    @Test
    public void testGetReplyByIdValid() {
        // Arrange
        int replyId = 1007;
        int reviewId = 2007;
        int gameId = 3007;
        String customerEmail = "customer1007@example.com";
        String managerEmail = "manager7384765@example.com";

        // Create Cart
        Cart cart = new Cart();

        // Create Game
        Game game = new Game("Test Game 7", "A test game 7", 70, Game.GameStatus.InStock, 70, "testPhotoUrl7");
        game.setGame_id(gameId);

        // Create Customer
        Customer customer = new Customer(customerEmail, "customerUser7", "customerPass7", "123-456-7890", "456 Customer Street 7", cart);

        // Create Manager
        Manager manager = new Manager(managerEmail, "managerUser7", "managerPass7", "123-456-7890", "123 Manager Street");

        // Create Review with Game and Customer
        Review review = new Review(LocalDate.parse("2023-10-15"), "Not bad", 3, Review.GameRating.Three, game, customer);
        review.setReview_id(reviewId);

        // Create Reply
        Reply reply = new Reply(Date.valueOf("2023-10-16"), "Thank you for your feedback", review, manager);
        reply.setReply_id(replyId);

        when(replyRepository.findById(replyId)).thenReturn(reply);

        // Act
        Reply retrievedReply = replyService.getReplyById(replyId);

        // Assert
        assertNotNull(retrievedReply);
        assertEquals(replyId, retrievedReply.getReply_id());
        assertEquals(reply.getDescription(), retrievedReply.getDescription());
        assertEquals(reply.getReplyDate(), retrievedReply.getReplyDate());
        assertEquals(review, retrievedReply.getReview());
        assertEquals(manager, retrievedReply.getManager());

        verify(replyRepository, times(1)).findById(replyId);
    }

    @Test
    public void testGetReplyByIdInvalid() {
        // Arrange
        int replyId = 1008; // Invalid reply ID

        when(replyRepository.findById(replyId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.getReplyById(replyId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Reply not found", exception.getMessage());

        verify(replyRepository, times(1)).findById(replyId);
    }

    // --- Tests for updateReply ---

    @Test
    public void testUpdateReplyValid() {
        // Arrange
        int replyId = 1009;
        int reviewId = 2009;
        int gameId = 3009;
        String customerEmail = "customer1009@example.com";
        String managerEmail = "manager384765349@example.com";

        // Create Cart
        Cart cart = new Cart();

        // Create Game
        Game game = new Game("Test Game 9", "A test game 9", 90, Game.GameStatus.InStock, 90, "testPhotoUrl9");
        game.setGame_id(gameId);

        // Create Customer
        Customer customer = new Customer(customerEmail, "customerUser9", "customerPass9", "123-456-7890", "456 Customer Street 9", cart);

        // Create Manager
        Manager manager = new Manager(managerEmail, "managerUser9", "managerPass9", "123-456-7890", "123 Manager Street");

        // Create Review with Game and Customer
        Review review = new Review(LocalDate.parse("2023-10-17"), "Great game!", 5, Review.GameRating.Five, game, customer);
        review.setReview_id(reviewId);

        // Create Reply
        Reply reply = new Reply(Date.valueOf("2023-10-18"), "Thanks!", review, manager);
        reply.setReply_id(replyId);

        String newDescription = "We appreciate your feedback!";
        ReviewRating newReviewRating = ReviewRating.Like;

        when(replyRepository.findById(replyId)).thenReturn(reply);
        when(replyRepository.save(any(Reply.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Reply updatedReply = replyService.updateReply(replyId, newDescription, newReviewRating);

        // Assert
        assertNotNull(updatedReply);
        assertEquals(replyId, updatedReply.getReply_id());
        assertEquals(newDescription, updatedReply.getDescription());
        assertEquals(newReviewRating, updatedReply.getReviewRating());

        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, times(1)).save(reply);
    }


    @Test
    public void testUpdateReplyWithNullDescriptionAndNullReviewRating() {
        // Arrange
        int replyId = 1010;
        int reviewId = 2010;
        int gameId = 3010;
        String customerEmail = "customer1010@example.com";
        String managerEmail = "manager13847630@example.com";

        // Create Cart
        Cart cart = new Cart();

        // Create Game
        Game game = new Game("Test Game 10", "A test game 10", 100, Game.GameStatus.InStock, 100, "testPhotoUrl10");
        game.setGame_id(gameId);

        // Create Customer
        Customer customer = new Customer(customerEmail, "customerUser10", "customerPass10", "123-456-7890", "456 Customer Street 10", cart);

        // Create Manager
        Manager manager = new Manager(managerEmail, "managerUser10", "managerPass10", "123-456-7890", "123 Manager Street");

        // Create Review with Game and Customer
        Review review = new Review(LocalDate.parse("2023-10-19"), "Average game", 3, Review.GameRating.Three, game, customer);
        review.setReview_id(reviewId);

        // Create Reply
        Reply reply = new Reply(Date.valueOf("2023-10-20"), "Thank you", review, manager);
        reply.setReply_id(replyId);

        String newDescription = null; // Null description
        ReviewRating newReviewRating = null; // Null review rating

        when(replyRepository.findById(replyId)).thenReturn(reply);
        when(replyRepository.save(any(Reply.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        // Act
        Reply updatedReply = replyService.updateReply(replyId, newDescription, newReviewRating);

        // Assert
        assertNotNull(updatedReply);
        assertEquals(replyId, updatedReply.getReply_id());
        assertEquals("Thank you", updatedReply.getDescription()); // Should remain unchanged
        assertEquals(reply.getReviewRating(), updatedReply.getReviewRating()); // Should remain unchanged

        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, times(1)).save(reply);
    }


    @Test
    public void testUpdateReplyWithInvalidReplyId() {
        // Arrange
        int replyId = 1011; // Invalid reply ID

        when(replyRepository.findById(replyId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.updateReply(replyId, "New description", ReviewRating.Like);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Reply not found", exception.getMessage());

        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, never()).save(any(Reply.class));
    }

    // --- Tests for getReviewByReplyId ---

    @Test
    public void testGetReviewByReplyIdValid() {
        // Arrange
        int replyId = 1012;
        int reviewId = 2012;
        int gameId = 3012;
        String customerEmail = "customer1012@example.com";
        String managerEmail = "manager1398462@example.com";

        // Create Cart
        Cart cart = new Cart();

        // Create Game
        Game game = new Game("Test Game 12", "A test game 12", 120, Game.GameStatus.InStock, 120, "testPhotoUrl12");
        game.setGame_id(gameId);

        // Create Customer
        Customer customer = new Customer(customerEmail, "customerUser12", "customerPass12", "123-456-7890", "456 Customer Street 12", cart);

        // Create Manager
        Manager manager = new Manager(managerEmail, "managerUser12", "managerPass12", "123-456-7890", "123 Manager Street");

        // Create Review with Game and Customer
        Review review = new Review(LocalDate.parse("2023-10-21"), "Fantastic game", 5, Review.GameRating.Five, game, customer);
        review.setReview_id(reviewId);

        // Create Reply
        Reply reply = new Reply(Date.valueOf("2023-10-22"), "Glad you enjoyed it!", review, manager);
        reply.setReply_id(replyId);

        when(replyRepository.findById(replyId)).thenReturn(reply);

        // Act
        Review retrievedReview = replyService.getReviewByReplyId(replyId);

        // Assert
        assertNotNull(retrievedReview);
        assertEquals(reviewId, retrievedReview.getReview_id());
        assertEquals(review.getDescription(), retrievedReview.getDescription());

        verify(replyRepository, times(1)).findById(replyId);
    }


    @Test
    public void testGetReviewByReplyIdInvalid() {
        // Arrange
        int replyId = 1013; // Invalid reply ID

        when(replyRepository.findById(replyId)).thenReturn(null);

        // Act & Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> {
            replyService.getReviewByReplyId(replyId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Reply not found", exception.getMessage());

        verify(replyRepository, times(1)).findById(replyId);
    }
}
