package com.mcgill.ecse321.GameShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyRequestDto;
import com.mcgill.ecse321.GameShop.dto.ReplyDto.ReplyResponseDto;
import com.mcgill.ecse321.GameShop.model.Reply;
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
}
