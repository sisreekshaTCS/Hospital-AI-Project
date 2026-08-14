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

@RestController
@RequestMapping("/api/v1/patients")
public class PatientChatController {
	
	private final PatientAssistantAgent patientAssistantAgent;

	public PatientChatController(PatientAssistantAgent patientAssistantAgent) {
		this.patientAssistantAgent = patientAssistantAgent;
	}

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
        String aiReply = patientAssistantAgent.answerPatientQuery(patientId, request.aiRequest(), patientId);

        // 2. Return HTTP 200 with the AI generated response
        return ResponseEntity.ok(new ChatResponse(aiReply));
    }
}
