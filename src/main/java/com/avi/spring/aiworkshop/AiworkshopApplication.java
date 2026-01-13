package com.avi.spring.aiworkshop;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class AiworkshopApplication {

	static void main(String[] args) {
		SpringApplication.run(AiworkshopApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			log.info("AI Workshop Application Started Successfully!");
			log.info("Using Spring Boot version: {}", SpringBootVersion.getVersion());
			log.info("OpenAI version: {}", ChatModel.class.getPackage().getImplementationVersion());
		};
	}
}
