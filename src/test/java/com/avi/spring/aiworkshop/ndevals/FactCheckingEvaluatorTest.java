package com.avi.spring.aiworkshop.ndevals;

import com.avi.spring.aiworkshop.AiModelsTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@Import(AiModelsTestConfig.class)
class FactCheckingEvaluatorTest {

    @Autowired
    ChatClient.Builder chatClientBuilder;

    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    void setUp() {
        log.info("Setting up FactCheckingEvaluator");
        factCheckingEvaluator = new FactCheckingEvaluator(
                chatClientBuilder,
                null // uses DEFAULT_EVALUATION_PROMPT_TEXT; check SpringAI library source for default prompt
        ) {
        };
    }

    @Test
    void testFactCheckingEvaluation() {
        log.info("Evaluating FactCheckingEvaluator");
        String userMessage = "When did India gain independence?";
        String llmResponseShouldBe = "India gained independence on August 15, 1947.";
        List<String> contexts = List.of("India gained independence from British rule on August 15, 1947, following a long struggle for freedom led by various leaders and movements.", "The independence movement was marked by significant events such as the Non-Cooperation Movement, Civil Disobedience Movement, and the Quit India Movement.", "The Indian Independence Act 1947, passed by the British Parliament, partitioned British India into two independent dominions: India and Pakistan.");

        EvaluationRequest evaluationRequest = new EvaluationRequest(userMessage, contextStringsToDocuments(contexts), llmResponseShouldBe);
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

        assertTrue(evaluationResponse.isPass(), "Claim should be factually correct. Feedback: " + evaluationResponse.getFeedback());
    }

    private List<Document> contextStringsToDocuments(List<String> contexts) {
        return contexts.stream().map(Document::new).toList();
    }
}
