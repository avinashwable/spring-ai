package com.avi.spring.aiworkshop.ndevals;

import com.avi.spring.aiworkshop.AiModelsTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(AiModelsTestConfig.class)
class RelevancyEvaluatorTest {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    RelevancyEvaluator relevancyEvaluator;

    @BeforeEach
    void setup() {
        relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @Test
    void testRelevancyEvaluation() {
        String userMessage = "What is the capital of India?";
        List<String> contexts = List.of(
                "India is a country in South Asia.",
                "The capital of India is New Delhi, but the financial capital is Mumbai.",
                "There are many languages spoken in different parts of India.",
                "Even though Hindi is the most widely spoken language, English is often used for official and business purposes because there is no official national language."
        );
        String llmResponseShouldBe = "The capital of India is New Delhi.";

        EvaluationRequest evaluationRequest = new EvaluationRequest(userMessage, contextStringsToDocuments(contexts), llmResponseShouldBe);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertTrue(evaluationResponse.isPass(), "Relevancy evaluation should pass.");
    }

    @Test
    void testRelevancyEvaluation1() {
        String userMessage = "What is the financial capital of India?";
        List<String> contexts = List.of(
                "India is a country in South Asia.",
                "The capital of India is New Delhi, but the financial capital is Mumbai.",
                "There are many languages spoken in different parts of India.",
                "Even though Hindi is the most widely spoken language, English is often used for official and business purposes because there is no official national language."
        );
        String llmResponseShouldBe = "The financial capital of India is Mumbai.";

        EvaluationRequest evaluationRequest = new EvaluationRequest(userMessage, contextStringsToDocuments(contexts), llmResponseShouldBe);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertTrue(evaluationResponse.isPass(), "Relevancy evaluation should pass.");
    }

    @Test
    void testRelevancyEvaluation2() {
        String userMessage = "What is the official national language of India?";
        List<String> contexts = List.of(
                "India is a country in South Asia.",
                "The capital of India is New Delhi, but the financial capital is Mumbai.",
                "There are many languages spoken in different parts of India.",
                "Even though Hindi is the most widely spoken language, English is often used for official and business purposes because there is no official national language."
        );
        String llmResponseShouldBe = "The official national language of India is Hindi.";

        EvaluationRequest evaluationRequest = new EvaluationRequest(userMessage, contextStringsToDocuments(contexts), llmResponseShouldBe);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        assertFalse(evaluationResponse.isPass(), "Relevancy evaluation should fail.");
    }

    private List<Document> contextStringsToDocuments(List<String> contexts) {
        return contexts.stream()
                .map(Document::new)
                .toList();
    }
}
