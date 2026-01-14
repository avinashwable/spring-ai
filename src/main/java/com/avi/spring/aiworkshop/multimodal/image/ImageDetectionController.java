package com.avi.spring.aiworkshop.multimodal.image;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// image detection using chat client passing the prompt with image media
@RestController
@RequestMapping("/image-detection")
public class ImageDetectionController {

    private final ChatClient chatClient;

    @Value("classpath:images/sample-image.jpg")
    Resource sampleImage;

    public ImageDetectionController(@Qualifier("openAiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/describe")
    public String describe() {
        return chatClient.prompt()
                .user(promptUserSpec -> {
                    promptUserSpec.text("Describe the objects in the given image.");
                    promptUserSpec.media(MimeTypeUtils.IMAGE_JPEG, sampleImage);
                })
                .call()
                .content();
    }
}
