package com.avi.spring.aiworkshop.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// guarded prompt example using system message
@RestController
@RequestMapping("/prompts")
public class PromptController {

    private final ChatClient promptClient;

    public PromptController(@Qualifier("openAiChatClient") ChatClient promptClient) {
        this.promptClient = promptClient;
    }

    // user message passed as is in the prompt. no guard rails
    @GetMapping("/chat")
    public String getPromptResponse(@RequestParam String message) {
        return promptClient.prompt()
                .user(message)
                .call()
                .content();
    }

    // user message passed with system message to enforce guard rails
    @GetMapping("/chat-guarded")
    public String getGuardedPromptResponse(@RequestParam String message) {
        String systemMessage = """
                You are a Customer Service Assistant for a car service station.
                You can only answer questions related to car servicing, maintenance, repairs and scheduling of service appointments and status of service works.
                If the question is not related to car servicing, maintenance, or repairs, politely inform the user that you can only assist with car service-related inquiries.
                """;
        return promptClient.prompt()
                .system(systemMessage) // adding guard rails, can also be added while creating the ChatClient bean which will be applied globally
                .user(message)
                .call()
                .content();
    }
}
