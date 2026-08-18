package dev.learning.rag.app;

import dev.learning.rag.app.config.RagProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Composition root: Spring starts the HTTP server and creates adapters declared
 * in {@code RagConfiguration}. Business logic remains plain Java in rag-core.
 * Study topics: dependency injection, composition roots, Spring Boot auto-configuration.
 */
@SpringBootApplication
@EnableConfigurationProperties(RagProperties.class)
public class MiniRagApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniRagApplication.class, args);
    }
}

