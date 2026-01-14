package com.avi.spring.aiworkshop.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// structured output example using entity mapping
@RestController
@RequestMapping("/vacation-plans")
public class VacationPlanController {

    private final ChatClient chatClient;

    public VacationPlanController(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/unstructured")
    public String getVacationPlanSuggestion() {
        String prompt = """
                Suggest a 5-day vacation plan for a family of four visiting Jammu & Kashmir for the first time.
                Include popular tourist attractions, dining options, and family-friendly activities.
                Itinerary should be balanced with sightseeing, relaxation, to have an immersive experience.
                Provide recommendations for accommodation and transportation within the region.
                """;
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @GetMapping("/structured")
    public Itinerary getStructuredVacationPlanSuggestion() {
        String prompt = """
                Suggest a 5-day vacation plan for a family of four visiting Jammu & Kashmir for the first time.
                Include popular tourist attractions, dining options, and family-friendly activities.
                Itinerary should be balanced with sightseeing, relaxation, to have an immersive experience.
                Provide recommendations for accommodation and transportation within the region.
                """;
        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(Itinerary.class);
    }
}
