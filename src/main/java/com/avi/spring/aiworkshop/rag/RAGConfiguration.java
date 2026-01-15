package com.avi.spring.aiworkshop.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Slf4j
@Configuration
public class RAGConfiguration {

    private static final String VECTOR_STORE_FILE = "src/main/resources/data/vectorstore.json";

    @Value("classpath:docs/java-virtual-threads-faq.txt")
    private Resource faqResource;

    @Value("classpath:data/vectorstore.json")
    private Resource vectorStoreResource;

    /**
     * This is just a simple example using SimpleVectorStore.
     * In real-world scenarios, consider using more robust vector stores like Pinecone, Weaviate, or FAISS.
     *
     * @param embeddingModel the embedding model to use for vectorization - this will be auto-configured by Spring AI
     *                      currently it injects {@link org.springframework.ai.openai.OpenAiEmbeddingModel}
     * @return SimpleVectorStore
     */
    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        log.info("Initializing SimpleVectorStore with FAQ document: {}", faqResource.getFilename());
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        File file = getVectorStoreFile();
        if (file.exists()) {
            log.info("Loading SimpleVectorStore from existing data file: {}", VECTOR_STORE_FILE);
            vectorStore.load(file);
        } else {
            log.info("Creating SimpleVectorStore with FAQ document: {}", faqResource.getFilename());
            TextReader textReader = new TextReader(faqResource);
            textReader.getCustomMetadata().put("fileName", faqResource.getFilename());
            List<Document> documents = textReader.read();
            TextSplitter splitter = new TokenTextSplitter();
            List<Document> chunkedDocuments = splitter.split(documents);

            vectorStore.add(chunkedDocuments);
            log.info("Saving SimpleVectorStore to data file: {}", VECTOR_STORE_FILE);
            vectorStore.save(file);
        }
        return vectorStore;
    }

    private File getVectorStoreFile() {
        return new File(VECTOR_STORE_FILE);
    }
}
