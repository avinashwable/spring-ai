package com.avi.spring.aiworkshop.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// chat with memory using ChatClient configured to use ChatMemory
@RestController
@RequestMapping("/memory")
public class MemoryController {

    private final ChatClient chatClient;
    private final ChatClient chatClientWithMemory;

    public MemoryController(@Qualifier("openAiChatClient") ChatClient chatClient,
                            @Qualifier("openAiChatClientWithMemory") ChatClient chatClientWithMemory) {
        this.chatClient = chatClient;
        this.chatClientWithMemory = chatClientWithMemory;
    }

    @GetMapping("/chat")
    public String normalPrompt(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/chat-memory")
    public String promptWithMemory(@RequestParam String message) {
        return chatClientWithMemory.prompt()
                .user(message)
                .call()
                .content();
    }
}
