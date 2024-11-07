package com.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyRequestDto;
import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyResponseDto;
import com.mcgill.ecse321.GameShop.dto.ReviewDto.ReviewResponseDto;
import com.mcgill.ecse321.GameShop.model.Reply;
import com.mcgill.ecse321.GameShop.model.Review;
import com.mcgill.ecse321.GameShop.service.ReplyService;

import jakarta.validation.Valid;

@RestController
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    /** Create a new reply */
    @PostMapping("/replies")
    public ReplyResponseDto createReply(@Valid @RequestBody ReplyRequestDto request) {
        Reply reply = replyService.createReply(
                request.getReplyDate(),
                request.getDescription(),
                request.getReviewRating(),
                request.getReviewId(),
                request.getManagerEmail()
        );
        return new ReplyResponseDto(reply);
    }

    /** Get a reply by ID */
    @GetMapping("/replies/{rid}")
    public ReplyResponseDto getReplyById(@PathVariable int rid) {
        Reply reply = replyService.getReplyById(rid);
        return new ReplyResponseDto(reply);
    }

    /** Update an existing reply */
    @PutMapping("/replies/{id}")
    public ReplyResponseDto updateReply(@PathVariable int id, @RequestBody ReplyRequestDto request) {
        Reply reply = replyService.updateReply(
                id,
                request.getDescription(),
                request.getReviewRating()
        );
        return new ReplyResponseDto(reply);
    }

    /** Get the review associated with a reply */
    @GetMapping("/replies/{replyId}/review")
    public ReviewResponseDto getReviewByReplyId(@PathVariable int replyId) {
        Review review = replyService.getReviewByReplyId(replyId);
        return new ReviewResponseDto(review);
    }
}
