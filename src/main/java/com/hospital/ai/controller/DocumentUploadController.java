package com.hospital.ai.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hospital.ai.service.DocumentIngestionService;

import lombok.AllArgsConstructor;

/*
 * REST controller for handling document uploads.
 * 
 * This controller exposes an endpoint for uploading medical reports (PDFs).
 * It receives the file, patient ID, and report type, and invokes the DocumentIngestionService
 * to process the document and store its vector embeddings.
 */
@RestController
@RequestMapping("/api/v1/documents")
@AllArgsConstructor
public class DocumentUploadController {
	private final DocumentIngestionService ingestionService;

	@PostMapping("/upload")
    public ResponseEntity<String> uploadMedicalReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") Long patientId,
            @RequestParam("reportType") String reportType) {
		
		System.out.println("--> Received file name: " + file.getOriginalFilename());
	    System.out.println("--> Received file size: " + file.getSize() + " bytes");

        try {
            ingestionService.ingestPDFReportDoc(file, patientId, reportType);
            return ResponseEntity.ok("Medical report ingested and vector embeddings stored successfully.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to process document: " + e.getMessage());
        }
    }

}
