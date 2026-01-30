package com.avi.spring.aiworkshop.evals;

import com.avi.spring.aiworkshop.ChatClientTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(ChatClientTestConfig.class)
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Test
    void testPositiveReview() {
        String reviewText = "I had an amazing experience at the restaurant! The food was delicious and the service was excellent.";
        Sentiment sentiment = reviewService.analyzeReview(reviewText);
        assertEquals(Sentiment.POSITIVE, sentiment);
    }

    @Test
    void testNegativeReview() {
        String reviewText = "The product was terrible and broke within a week. I'm very disappointed with my purchase.";
        Sentiment sentiment = reviewService.analyzeReview(reviewText);
        assertEquals(Sentiment.NEGATIVE, sentiment);
    }

    @Test
    void testNeutralReview() {
        String reviewText = "The movie was okay. It had some good moments, but overall it was just average.";
        Sentiment sentiment = reviewService.analyzeReview(reviewText);
        assertEquals(Sentiment.NEUTRAL, sentiment);
    }
}
