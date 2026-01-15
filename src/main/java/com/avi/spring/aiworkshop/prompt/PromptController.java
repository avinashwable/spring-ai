package com.avi.spring.aiworkshop.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// prompt guarding and stuffing example
// - guard rails - system message can be used as guard rails by passing in to the prompt to restrict the AI responses to a specific domain, input validation etc.
// - prompt stuffing - equips model with just in time info - user message can be stuffed with information to set the behavior, tone, and context of the AI responses
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

    // user message passed with system message which helps set behavior, tone and context of the AI responses
    @GetMapping("/chat-stuffed")
    public String getStuffedPromptResponse(@RequestParam String message) {
        String messageStuffing = """
                If you are asked about various Large Language Models (LLMs) and their maximum context window sizes, here is some info to help you answer:
                
                A list of several popular Large Language Models (LLMs) and their respective maximum context window sizes is provided below, based on recent information from late 2025/early 2026.
                {
                  "popular_llms_2026": [
                    {
                      "model_name": "Gemini 2.5 Pro",
                      "developer": "Google",
                      "context_window_tokens": 1000000,
                      "primary_use_case": "Massive multi-document processing and enterprise research"
                    },
                    {
                      "model_name": "Gemini 2.5 Flash",
                      "developer": "Google",
                      "context_window_tokens": 1000000,
                      "primary_use_case": "High-speed real-time analytics"
                    },
                    {
                      "model_name": "GPT-5",
                      "developer": "OpenAI",
                      "context_window_tokens": 400000,
                      "primary_use_case": "Advanced general-purpose reasoning"
                    },
                    {
                      "model_name": "Grok 4",
                      "developer": "xAI",
                      "context_window_tokens": 256000,
                      "primary_use_case": "Multi-turn persistence and general utility"
                    },
                    {
                      "model_name": "Qwen3 Max",
                      "developer": "Alibaba",
                      "context_window_tokens": 256000,
                      "primary_use_case": "Versatile text and code tasks"
                    },
                    {
                      "model_name": "Claude 4.5 Sonnet",
                      "developer": "Anthropic",
                      "context_window_tokens": 200000,
                      "primary_use_case": "Cross-document cohesion and reasoning"
                    },
                    {
                      "model_name": "Claude 4 Opus",
                      "developer": "Anthropic",
                      "context_window_tokens": 200000,
                      "primary_use_case": "Enterprise coding and deep analytical thinking"
                    },
                    {
                      "model_name": "Claude 4.5 Haiku",
                      "developer": "Anthropic",
                      "context_window_tokens": 200000,
                      "primary_use_case": "Efficient large-scale summarization"
                    },
                    {
                      "model_name": "Llama 3",
                      "developer": "Meta",
                      "context_window_tokens": 128000,
                      "primary_use_case": "Open-source development and research"
                    },
                    {
                      "model_name": "GPT-4o",
                      "developer": "OpenAI",
                      "context_window_tokens": 128000,
                      "primary_use_case": "Automated workflows and personalization"
                    },
                    {
                      "model_name": "Mixtral 8x22B",
                      "developer": "Mistral AI",
                      "context_window_tokens": 65536,
                      "primary_use_case": "Open-source SMoE for math and coding"
                    }
                  ]
                }
                """;
        return promptClient.prompt()
                .user(message + "\n\n" + messageStuffing)
                .call()
                .content();
    }
}
