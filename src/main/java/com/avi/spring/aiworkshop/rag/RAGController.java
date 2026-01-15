package com.avi.spring.aiworkshop.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/*******************************************************************************************************************************************************************
 * Prompt stuffing works well for small context, but for larger context we need to use Retrieval Augmented Generation (RAG) approach.
 * RAG involves two main steps:
 * 1. Document Ingestion (Offline ETL): In this step, we ingest documents, split them into smaller chunks, and store them in a vector store.
 *     - DataSource: The source of the documents to be ingested.
 *     - Reader: Reads the documents from the DataSource.
 *     - Transformer: Splits the documents into smaller chunks.
 *     - Writer: Writes the chunks to the Vector Store.
 * 2. Retrieval Augmented Generation (RAG) (Runtime): In this step, we retrieve relevant chunks from the vector store based on the user query,
 *    augment the prompt with these chunks, and then generate the response using a chat model.
 * <p>
 * The diagram below illustrates the RAG process:
 *                   ┌───────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
 *                   │  DOCUMENT INGESTION – ETL (OFFLINE)                                                                           │
 *                   │                                                             ┌─────┐                                           │
 *                   │                          ┌─────┐                            │  C  │                                           │
 *                   │                          │  D  │                            │-----│                                           │
 *                   │         <<Read>>         │  O  │          <<Split>>         │  H  │       <<Write>>                           │
 *   ┌────────────┐  │       ┌──────────┐       │  C  │       ┌─────────────┐      │-----│      ┌──────────┐                         │
 *   │ DataSource │──┼─────► │  Reader  │ ────► │  U  │ ────► │ Transformer │ ────►│  U  │ ────►│  Writer  │ ────┐                   │
 *   └────────────┘  │       └──────────┘       │  M  │       └─────────────┘      │-----│      └──────────┘     │                   │
 *                   │                          │  E  │                            │  N  │                       │                   │
 *                   │                          │  N  │                            │-----│                       │                   │
 *                   │                          │  T  │                            │  K  │                       │                   │
 *                   │                          │  s  │                            │-----│                       │                   │
 *                   │                          └─────┘                            │  s  │                       │                   │
 *                   │                                                             └─────┘                       │                   │
 *                   │                                                                                           │                   │
 *                   │                                ┌──────────────────────────────────────────────────────────┘                   │
 *                   │                                │                                                                              │
 *                   │                                ▼                                                                              │
 *                   │                       ┌─────────────────┐                                                                     │
 *                   │                       │                 │                                                                     │
 *                   │                       │   VECTOR STORE  │                                                                     │
 *                   │                       │                 │                                                                     │
 *                   └───────────────────────│                 │─────────────────────────────────────────────────────────────────────┘
 * Offline                                   │                 │
 * ------------------------------------------┼-----------------┼----------------------------------------------------------------------------------------------------
 * Online                                    │                 │
 *                   ┌───────────────────────│                 │─────────────────────────────────────────────────────────────────────┐
 *                   │                       │                 │                                                                     │
 *                   │                       │                 │                                                                     │
 *                   │             ┌────────►│                 │───────┐                                                             │
 *                   │             │         │                 │       │                                                             │
 *                   │             │         └─────────────────┘       │                                                             │
 *                   │       <<Retrieve>>                              │                                                             │
 *                   │             │                                   ▼                                                             │
 *  ┌──────────────┐ │       ┌────────────┐                     ┌──────────────┐                                                     │
 *  │ Chat Request │─┼──────►│   Prompt   │                     │ Query Chunks │                                                     │
 *  └──────────────┘ │       │────────────│                     └──────────────┘                                                     │
 *                   │       │ User Query │                            │               ┌───────────────────────┐                     │
 *                   │       └────────────┘                            │               │   Augmented Prompt    │                     │
 *                   │             │                                   ▼               │───────────────────────│     ┌────────────┐  │   ┌───────────────┐
 *                   │             │                              <<Augment>>─────────►│      User Query       │────►│ Chat Model │──┼──►│ Chat Response │
 *                   │             │                                   ▲               │  Context Information  │     └────────────┘  │   └───────────────┘
 *                   │             │                                   │               │                       │                     │
 *                   │             └───────────────────────────────────┘               └───────────────────────┘                     │
 *                   │                                                                                                               │
 *                   │                                                                                                               │
 *                   │                                                                                                               │
 *                   │                                                                                                               │
 *                   │  RETRIEVAL AUGMENTED GENERATION – RAG (RUNTIME)                                                               │
 *                   └───────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
 *
 *
 ******************************************************************************************************************************************************************/
@RestController
@RequestMapping("/rag")
public class RAGController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:templates/rag-prompt-template.txt")
    private Resource promptTemplateResource;

    public RAGController(@Qualifier("openAiChatClient") ChatClient chatClient, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    // user message passed as is in the prompt. no guard rails
    @GetMapping("/faq")
    public String getResponse(@RequestParam String message) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(message)
                .topK(5)
                .build();
        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
        List<String> contextTexts = similarDocs.stream()
                .map(Document::getText)
                .toList();
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource);
        Map<String, Object> promptParams = Map.of("input", message, "documents", String.join("\n", contextTexts));
        Prompt prompt = promptTemplate.create(promptParams);
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
