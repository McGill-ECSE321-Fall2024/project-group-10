package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;

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
        int replyId = 1001; // Unique reply ID
        int reviewId = 2001; // Unique review ID
        String managerEmail = "manager1034879@example.com"; // Unique manager email
        Date replyDate = Date.valueOf("2023-10-10");
        String description = "Thank you for your feedback!";
        ReviewRating reviewRating = ReviewRating.Like;

        // Create Manager and Review
        Manager manager = new Manager(managerEmail, "managerUser1", "managerPass1", "123-456-7890", "123 Manager Street");
        Review review = new Review(Date.valueOf("2023-10-01"), "Great game!", 5, Review.GameRating.Five, null, null);
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
        int reviewId = 2002; // Unique review ID
        String managerEmail = "manager243874638@example.com"; // Unique manager email
        Date replyDate = null; // Null date
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
        int reviewId = 2003; // Unique review ID
        String managerEmail = "manager293873@example.com"; // Unique manager email
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
        int reviewId = 2004; // Unique review ID
        String managerEmail = "manager44339476@example.com"; // Unique manager email
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
        int reviewId = 2005; // Unique review ID
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
        int replyId = 1007; // Unique reply ID
        int reviewId = 2007; // Unique review ID
        String managerEmail = "manager7384765@example.com"; // Unique manager email

        Manager manager = new Manager(managerEmail, "managerUser7", "managerPass7", "123-456-7890", "123 Manager Street");
        Review review = new Review(Date.valueOf("2023-10-15"), "Not bad", 3, Review.GameRating.Three, null, null);
        review.setReview_id(reviewId);
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
        assertEquals(reply.getReview(), retrievedReply.getReview());
        assertEquals(reply.getManager(), retrievedReply.getManager());

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
        int replyId = 1009; // Unique reply ID
        int reviewId = 2009; // Unique review ID
        String managerEmail = "manager384765349@example.com"; // Unique manager email

        Manager manager = new Manager(managerEmail, "managerUser9", "managerPass9", "123-456-7890", "123 Manager Street");
        Review review = new Review(Date.valueOf("2023-10-17"), "Great game!", 5, Review.GameRating.Five, null, null);
        review.setReview_id(reviewId);
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
        int replyId = 1010; // Unique reply ID
        int reviewId = 2010; // Unique review ID
        String managerEmail = "manager13847630@example.com"; // Unique manager email

        Manager manager = new Manager(managerEmail, "managerUser10", "managerPass10", "123-456-7890", "123 Manager Street");
        Review review = new Review(Date.valueOf("2023-10-19"), "Average game", 3, Review.GameRating.Three, null, null);
        review.setReview_id(reviewId);
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
        int replyId = 1012; // Unique reply ID
        int reviewId = 2012; // Unique review ID
        String managerEmail = "manager1398462@example.com"; // Unique manager email

        Manager manager = new Manager(managerEmail, "managerUser12", "managerPass12", "123-456-7890", "123 Manager Street");
        Review review = new Review(Date.valueOf("2023-10-21"), "Fantastic game", 5, Review.GameRating.Five, null, null);
        review.setReview_id(reviewId);
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
