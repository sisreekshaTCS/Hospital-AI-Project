package com.hospital.ai.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.AllArgsConstructor;

/**
 * Service class responsible for ingesting PDF medical reports into the embedding store.
 * It parses the PDF, splits it into text segments, generates embeddings, and stores them with strict patient metadata.
 */
@Service
@AllArgsConstructor
public class DocumentIngestionService {
	
	private final EmbeddingStore<TextSegment> embeddingStore;
	private final EmbeddingModel embeddingModel;
	
	public void ingestPDFReportDoc(MultipartFile pdfFile, Long patientId, String reportType) throws IOException {
		
		if (pdfFile.isEmpty()) {
	        throw new IllegalArgumentException("File stream is empty");
	    }
		
		try(InputStream inputStream = pdfFile.getInputStream()) {
			
			// 1. Parse PDF document
            Document document = new ApacheTikaDocumentParser().parse(inputStream);

            // 2. Attach metadata for strict patient data isolation
            document.metadata().put("patient_id", patientId.toString());
            document.metadata().put("report_type", reportType);
			
			EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
					.documentSplitter(DocumentSplitters.recursive(500,50))
					.embeddingModel(embeddingModel)
					.embeddingStore(embeddingStore)
					.build();
			
			ingestor.ingest(document);
            System.out.println("--> [RAG INGESTION]: Successfully ingested report for patient: " + patientId);
		}
	}

}
