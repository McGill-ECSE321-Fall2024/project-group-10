package com.mcgill.ecse321.GameShop.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.model.Reply;
import com.mcgill.ecse321.GameShop.model.Reply.ReviewRating;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.ReplyRepository;
import com.mcgill.ecse321.GameShop.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // Helper method to check if a string is empty or null
    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Create a new Reply.
     *
     * @param replyDate     The date of the reply.
     * @param description   The description of the reply.
     * @param reviewRating  The rating of the review (Like or Dislike).
     * @param reviewId      The ID of the review being replied to.
     * @param managerEmail  The email of the manager creating the reply.
     * @return The created Reply.
     */
    @Transactional
    public Reply createReply(Date replyDate, String description, ReviewRating reviewRating, int reviewId, String managerEmail) {

        // Validate inputs
        if (replyDate == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Reply date cannot be null");
        }
        if (isEmpty(description)) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Description cannot be empty or null");
        }
        if (reviewRating == null) {
            throw new GameShopException(HttpStatus.BAD_REQUEST, "Review rating cannot be null");
        }

        Manager manager = managerRepository.findByEmail(managerEmail);
        if (manager == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Manager with email %s not found", managerEmail));
        }

        Review review = reviewRepository.findById(reviewId);
        if (review == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, String.format("Review with ID %d not found", reviewId));
        }

        // Create the reply
        Reply reply = new Reply(replyDate, description, review, manager);
        reply.setReviewRating(reviewRating);

        // Save and return the reply
        return replyRepository.save(reply);
    }

    /**
     * Retrieve a Reply by its ID.
     *
     * @param replyId The ID of the reply.
     * @return The Reply object.
     */
    @Transactional
    public Reply getReplyById(int replyId) {
        Reply reply = replyRepository.findById(replyId);
        if (reply == null) {
            throw new GameShopException(HttpStatus.NOT_FOUND, "Reply not found");
        }
        return reply;
    }

    /**
     * Update an existing Reply.
     *
     * @param replyId      The ID of the reply to update.
     * @param description  The new description (optional).
     * @param reviewRating The new review rating (optional).
     * @return The updated Reply.
     */
    @Transactional
    public Reply updateReply(int replyId, String description, ReviewRating reviewRating) {
        Reply reply = getReplyById(replyId);

        // Update description if provided
        if (!isEmpty(description)) {
            reply.setDescription(description);
        }

        // Update review rating if provided
        if (reviewRating != null) {
            reply.setReviewRating(reviewRating);
        }

        // Save and return the updated reply
        return replyRepository.save(reply);
    }
}