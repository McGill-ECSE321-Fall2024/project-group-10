package com.mcgill.ecse321.GameShop.service;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.model.Review.GameRating;
import com.mcgill.ecse321.GameShop.repository.ReviewRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock ReviewService reviewService;
    
    private static final String VALID_DESCRIPTION = "This is a valid description";
    private static final GameRating VALID_GAME_RATING = GameRating.Five;
    private static int VALID_RATING = 1;
    
}