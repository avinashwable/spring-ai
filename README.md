# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.9/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.9/gradle-plugin/packaging-oci-image.html)
* [OpenAI](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.9/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)



### Unit testing AI applications
* Why to evaluate AI responses? - Because AI responses are non-deterministic
  - hallucinations - AI models can generate plausible but incorrect or nonsensical answers
  - consistency - AI models may produce different outputs for the same input
  - bias and fairness - AI models can reflect and amplify societal biases present in training data
  - performance - measuring response time and resource usage is important for user experience
  - safety and compliance - ensuring AI responses adhere to ethical guidelines and legal requirements
* Approaches to evaluate AI responses
  - Automated testing - using predefined test cases to check for expected outputs - this mainly involves evaluating AI response using AI itself
  - Human evaluation - involving human reviewers to assess the quality and relevance of AI responses
  - Benchmarking - comparing AI model performance against established benchmarks or datasets
  - A/B testing - comparing different versions of AI models to determine which performs better in real-world scenarios
  - Continuous monitoring - tracking AI model performance over time to identify and address issues promptly
* Tools and frameworks for testing AI applications
  - OpenAI's evaluation tools - tools provided by OpenAI for evaluating model performance
  - Custom scripts - writing custom scripts to automate testing and evaluation processes
  - Third-party testing frameworks - using existing testing frameworks that support AI model evaluation
* Best practices for testing AI applications
  - Define clear evaluation criteria - establish what constitutes a successful AI response
  - Use diverse test cases - ensure test cases cover a wide range of scenarios and inputs
  - Involve domain experts - include experts in the relevant field to assess AI responses
  - Regularly update test cases - keep test cases current with evolving AI models and use cases
  - Document findings - maintain records of evaluation results to inform future improvements
  - Incorporate feedback loops - use evaluation results to refine and enhance AI models continuously
  - Ethical considerations - ensure testing processes respect user privacy and data security
  - Scalability - design testing frameworks that can handle increasing amounts of data and complexity as AI applications grow
  - Collaboration - foster collaboration between AI developers, testers, and stakeholders to ensure comprehensive evaluation
  - Automation - leverage automation to streamline testing processes and reduce manual effort
  - Real-world scenarios - test AI applications in real-world conditions to assess performance and reliability
  - Cross-functional teams - involve diverse teams to bring different perspectives to AI evaluation
  - Feedback integration - incorporate user feedback into the evaluation process to enhance AI applications
  - Continuous improvement - use evaluation results to drive ongoing enhancements to AI models and applications
  - Transparency - maintain transparency in evaluation methods and results to build trust with users and stakeholders
  - Regulatory compliance - ensure AI applications meet relevant regulatory standards and guidelines through thorough testing and evaluation
  - User-centric approach - prioritize user needs and experiences in the evaluation process to ensure AI applications deliver value and satisfaction
  - Performance metrics - establish and track key performance indicators (KPIs) to measure AI application success and identify areas for improvement
  - Cross-validation - use cross-validation techniques to assess AI model generalizability and robustness across different datasets and scenarios
* What Spring AI provides to help with testing AI applications
  - Spring AI Test module - provides utilities and abstractions to facilitate testing of AI applications built with Spring AI
  - Mocking capabilities - allows developers to mock AI model responses for controlled testing scenarios
  - Integration with testing frameworks - seamless integration with popular testing frameworks like JUnit and Mockito for comprehensive test coverage
  - Predefined test cases - includes a set of predefined test cases to evaluate common AI functionalities and behaviors
  - Customizable evaluation criteria - enables developers to define and implement their own evaluation criteria for AI responses
  - Reporting and analytics - provides tools for generating reports and analyzing test results to identify areas for improvement
  - Continuous integration support - integrates with CI/CD pipelines to automate testing and ensure consistent quality in AI applications

### Spring AI Evaluation Module

## Testing non-deterministic AI responses

* Core interface - Evaluator
```java
@FunctionalInterface
public interface Evaluator {
    EvaluationResponse evaluate(EvaluationRequest evaluationRequest);
}
```
* EvaluationRequest has:
 - user input
 - context data (e.g. from RAG)
 - response from AI model

* There are two main types of evaluators provided:

RelevancyEvaluator - checks if the response is relevant to the user input and context data
 - Asks - "Does this response answer the user's question?", and returns YES/NO
   - Default prompt template
      Your task is to evaluate if the response for the query is in line with the context information provided.
      Answer YES if relevant, otherwise NO.
   - Best for
     - RAG
     - Ensuring responses are on-topic
     - Quality control for chatbots

FactCheckingEvaluator - checks if the response is factually correct
 - Verifies claims made in the response against the provided context data
   - Detects factual inaccuracies and hallucinations
   - Can use specialized models like Bespoke-Minicheck (this is accurate, small, fast and cost-effective model for fact-checking)
   - Evaluation format
      Document: {context}
      Claim: {response}
   - Best for
     - Detecting hallucinations
     - Verifying claims against source materials or Knowledge-based applications
     - Content validation - Ensuring accuracy of information provided by AI models

## Testing deterministic AI responses
* Use traditional unit testing approaches
* Examples:
  - Sentiment analysis - positive/negative/neutral
  - Content moderation - flagging inappropriate content safe/unsafe
  - Intent Detection - question/request/complaint
* Code example
```java
@Test
void testSentiment() {
    String input = "I love this product!";
    String expectedSentiment = "positive";

    String actualSentiment = aiService.analyzeSentiment(input);
    assertEquals(expectedSentiment, actualSentiment);
}
```
