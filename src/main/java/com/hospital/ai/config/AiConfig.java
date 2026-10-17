package com.hospital.ai.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

/**
 * Configuration class for setting up 
 * 1. Chat language AI model (Gemini) and
 * 2. embedding AI model and 
 * 3. Chat memory provider.
 */

@Configuration
public class AiConfig {
	
	@Value("${gemini.api.key}")
    private String apiKey;

    @Bean
    public GoogleAiGeminiChatModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
        		.apiKey(apiKey)
                .modelName("gemini-3.6-flash")
                .temperature(0.0)
                .timeout(Duration.ofSeconds(30))
                .sendThinking(true)    // Preserves reasoning state on multi-turn function calls
                .returnThinking(true)  // Captures thought signatures returned by Gemini
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return GoogleAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-embedding-001")
                .timeout(Duration.ofSeconds(30))
                .build();
    }
	
	/**
     * Maintains a rolling window of the last 10 messages 
     * in memory per unique patient ID.
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(10);
    }

}
