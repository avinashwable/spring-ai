package com.avi.spring.aiworkshop.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// templating example using PromptUserSpec for article writing
@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/new")
    public String newPost(@RequestParam String topic) {
        String systemMessage = """
                You are an expert article writer. Use the following guidelines to write the article:
                1. Length & purpose: Aim for 500 words focusing on the essence of the topic.
                2. Structure: Organize the article with a clear introduction, main body, and conclusion.
                3. Clarity: Use clear and concise language to make the content easily understandable.
                4. Examples: Provide relevant examples to illustrate key points and enhance understanding.
                5. Engagement: Write in an engaging manner to keep the reader interested throughout the article
                6. Response: Give complete ready to publish post with good title.
                """;

        String prompt = "Write a comprehensive article on the topic {topic}";

        return chatClient.prompt()
                .system(systemMessage)
                .user(promptUserSpec -> {
                    promptUserSpec.text(prompt);
                    promptUserSpec.param("topic", topic);
                })
                .call()
                .content();
    }
}
