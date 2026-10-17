package com.hospital.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.ai.dto.ChatRequest;
import com.hospital.ai.dto.ChatResponse;
import com.hospital.ai.service.PatientAssistantAgent;

import lombok.AllArgsConstructor;

/**
 * REST controller for handling patient chat requests.
 * 
 * This controller exposes an endpoint for patients to interact with the AI assistant.
 * It receives chat requests, invokes the PatientAssistantAgent to process the request,
 * and returns the AI-generated response.
 */

@RestController
@RequestMapping("/api/v1/patients")
@AllArgsConstructor
public class PatientChatController {
	
	private final PatientAssistantAgent patientAssistantAgent;

	@PostMapping("/{patientId}/chat")
    public ResponseEntity<ChatResponse> chatWithAssistant(
            @PathVariable Long patientId,
            @RequestBody ChatRequest request) {

		// Sanity check to prevent null prompts
	    if (request == null || request.aiRequest() == null) {
	        return ResponseEntity.badRequest().body(new ChatResponse("Message cannot be empty."));
	    }
	    
        // 1. Invoke the AI Agent
        // This triggers: History Lookup -> Tool Execution (if needed) -> LLM Inference
	    System.out.println("--> [RAG CHAT]: Received chat request from patient: " + patientId + " with message: " + request.aiRequest());
        String aiReply = patientAssistantAgent.answerPatientQuery(patientId, request.aiRequest(), patientId);

        // 2. Return HTTP 200 with the AI generated response
        return ResponseEntity.ok(new ChatResponse(aiReply));
    }
}
