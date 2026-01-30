package com.avi.spring.aiworkshop.evals;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ChatClient chatClient;

    public ReviewService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Sentiment analyzeReview(String reviewText) {
        String systemPrompt = "Analyze the sentiment of the following review and respond with either POSITIVE, NEGATIVE, or NEUTRAL";

        return chatClient.prompt()
                .system(systemPrompt)
                .user(reviewText)
                .call()
                .entity(Sentiment.class);
    }
}
