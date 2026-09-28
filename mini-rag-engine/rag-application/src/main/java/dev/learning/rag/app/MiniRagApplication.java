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
    /**
     * Backend flow: this is the ordinary Spring Boot process entry point. The
     * call creates the application context, discovers {@code @Configuration}
     * and {@code @RestController} classes, starts the embedded HTTP server, and
     * waits for requests. No RAG algorithm runs here.
     *
     * <p>How to evolve it: application behavior should normally be added as a
     * bean or service, not inside {@code main}. Keep this method stable so the
     * application starts the same way in an IDE, a JAR, Docker, or tests.</p>
     */
    public static void main(String[] args) {
        SpringApplication.run(MiniRagApplication.class, args);
    }
}
