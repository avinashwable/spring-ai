plugins {
	java
	id("org.springframework.boot") version "4.0.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.avi.spring"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot AI"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

extra["springAiVersion"] = "2.0.0-M1"

dependencies {
	// for exposing REST endpoints and WebFlux support
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Spring AI Starter for OpenAI Models
	implementation("org.springframework.ai:spring-ai-starter-model-openai")
	// Spring AI Starter for Anthropic Models
	implementation("org.springframework.ai:spring-ai-starter-model-anthropic")

	implementation("io.netty:netty-resolver-dns-native-macos") {
		artifact {
			classifier = "osx-aarch_64"
		}
	}

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Lombok for main code
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	// Lombok for test code
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
