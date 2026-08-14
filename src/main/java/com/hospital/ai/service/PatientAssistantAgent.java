package com.hospital.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface PatientAssistantAgent {

    @SystemMessage("""
        You are an empathetic, precise Medical Assistant for our Hospital.
        You have access to tools to query structured patient records like prescriptions.
        
        CRITICAL RULES:
        1. You MUST use your available tools to check patient data before answering questions about medications.
        2. Never alter prescription details or diagnose new conditions.
        3. If you don't find data via your tools, inform the patient politely.
    """)
    String answerPatientQuery(
            @MemoryId Long memoryId,
            @UserMessage String userMessage,
            @V("patientId") Long patientId
    );
}
