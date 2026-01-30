package com.avi.spring.aiworkshop.output;

import com.avi.spring.aiworkshop.ChatClientTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(ChatClientTestConfig.class)
class StructuredOutputTest {

    @Autowired
    private ChatClient chatClient;

    @Test
    void testGetItinerary() {
        String destination = "Konkan Coast";
        String prompt = "Generate a 3-day itinerary for a trip to {destination}, including activities, accommodations and dining options.";
        Itinerary itinerary = chatClient.prompt()
                .user(u -> {
                    u.text(prompt);
                    u.param("destination", destination);
                })
                .call()
                .entity(Itinerary.class);

        assertNotNull(itinerary);
        assertNotNull(itinerary.accommodations());
        assertNotNull(itinerary.activities());
    }
}
