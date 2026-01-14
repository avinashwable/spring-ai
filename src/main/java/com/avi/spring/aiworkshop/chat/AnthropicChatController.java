package com.avi.spring.aiworkshop.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/anthropic")
public class AnthropicChatController {

    private final ChatClient chatClient;

    public AnthropicChatController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public String chat() {
        return chatClient.prompt()
                .user("Tell me what is Spring AI in one sentence and the latest version available?")
                .call()
                .content();
    }

    @GetMapping("/chat-stream")
    public Flux<String> chatStream() {
        return chatClient.prompt()
                .user("Give live commentary of Ussain Bolt running 100m sprint in the Olympics, one line at a time.")
                .stream()
                .content();
    }

    @GetMapping("/chat-response")
    public ChatResponse chatFunction() {
        return chatClient.prompt()
                .user("Get me the current weather in Pune City.")
                .call()
                .chatResponse();
    }
}
