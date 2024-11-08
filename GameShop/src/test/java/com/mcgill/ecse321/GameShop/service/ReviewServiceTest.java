package com.mcgill.ecse321.GameShop.service;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse321.GameShop.repository.ReviewRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock ReviewService reviewService;
    
    private static final int VALID_WISHLIST_ID = 1;
    private static final String VALID_CUSTOMER_EMAIL = "customer@example.com";
    private static int VALID_GAME_ID;
}
