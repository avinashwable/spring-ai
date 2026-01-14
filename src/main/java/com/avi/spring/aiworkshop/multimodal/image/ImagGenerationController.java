package com.avi.spring.aiworkshop.multimodal.image;

import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// image generation using OpenAI image model
@RestController
@RequestMapping("/image-generation")
public class ImagGenerationController {

    private final OpenAiImageModel imageModel;

    public ImagGenerationController(OpenAiImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestParam String prompt) {
        ImageOptions options = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .width(1024)
                .height(1024)
                .quality("hd")
                .style("photorealistic")
                .build();
        ImageResponse response = imageModel.call(new ImagePrompt(prompt, options));
        return ResponseEntity.ok(Map.of(
                "prompt", prompt,
                "imageUrl", response.getResult().getOutput().getUrl()
        ));
    }
}
