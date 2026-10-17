package com.hospital.ai.tools;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import lombok.AllArgsConstructor;

/**
 * Service class that provides tools for retrieving relevant snippets from medical reports, blood tests, and scan summaries
 * for a specific patient using vector search (RAG - Retrieval-Augmented Generation).
 * 
 * It generates embeddings for the search query, applies strict filtering based on patient ID, and retrieves the top relevant text segments.
 */
@Service
@AllArgsConstructor
public class MedicalReportRagTools {
	
	private final EmbeddingStore<TextSegment> embeddingStore;
	private final EmbeddingModel embeddingModel;

	@Tool("Searches and retrieves relevant snippets from medical reports, blood tests, and scan summaries for a specific patient")
	public List<String> ingestMedicalReport(
			@P("The detailed search query or medical term to look up") String query,
            @P("The unique numeric ID of the patient") Long patientId) throws IOException {
		
		System.out.println("--> [RAG TOOL CALLED]: Vector search for patient ID: " + patientId + " Query: " + query);
		
		// 1. Generate vector embedding for the search query
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        // 2. Strict Filter: Only return vectors where metadata patient_id matches current patient
        Filter patientFilter = new IsEqualTo("patient_id", patientId.toString());

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .filter(patientFilter)
                .maxResults(3)       // Top 3 relevant chunks
                .minScore(0.6)       // Similarity threshold
                .build();

        // 3. Query pgvector
        List<EmbeddingMatch<TextSegment>> matches = embeddingStore.search(searchRequest).matches();

        return matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.toList());
	}
}
