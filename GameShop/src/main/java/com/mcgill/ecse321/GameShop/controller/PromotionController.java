package com.mcgill.ecse321.GameShop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mcgill.ecse321.GameShop.dto.PromotionDto.PromotionListDto;
import com.mcgill.ecse321.GameShop.dto.PromotionDto.PromotionRequestDto;
import com.mcgill.ecse321.GameShop.dto.PromotionDto.PromotionResponseDto;
import com.mcgill.ecse321.GameShop.dto.PromotionDto.PromotionSummaryDto;
import com.mcgill.ecse321.GameShop.model.Promotion;
import com.mcgill.ecse321.GameShop.service.PromotionService;

import jakarta.validation.Valid;

@RestController
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    /** Create a new promotion */
    @PostMapping("/promotions")
    public PromotionResponseDto createPromotion(@Valid @RequestBody PromotionRequestDto request) {
        Promotion promotion = promotionService.createPromotion(
                request.getDescription(),
                request.getDiscountRate(),
                request.getStartDate(),
                request.getEndDate(),
                request.getManagerEmail()
        );
        return new PromotionResponseDto(promotion);
    }

    /** Get a promotion by ID */
    @GetMapping("/promotions/{pid}")
    public PromotionResponseDto getPromotionById(@PathVariable int pid) {
        Promotion promotion = promotionService.getPromotionById(pid);
        return new PromotionResponseDto(promotion);
    }

    /** Get all promotions */
    @GetMapping("/promotions")
    public PromotionListDto getAllPromotions() {
        List<PromotionSummaryDto> dtos = new ArrayList<>();
        for (Promotion promotion : promotionService.getAllPromotions()) {
            dtos.add(new PromotionSummaryDto(promotion));
        }
        return new PromotionListDto(dtos);
    }

    /** Update a promotion */
    @PutMapping("/promotions/{id}")
    public PromotionResponseDto updatePromotion(@PathVariable int id, @RequestBody PromotionRequestDto request) {
        Promotion promotion = promotionService.updatePromotion(
                id,
                request.getDescription(),
                request.getDiscountRate(),
                request.getStartDate(),
                request.getEndDate()
        );
        return new PromotionResponseDto(promotion);
    }
}