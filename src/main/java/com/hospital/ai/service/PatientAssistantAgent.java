package com.hospital.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * PatientAssistantAgent is an AI service interface that defines the contract for interacting with patients.
 * It provides a method to answer patient queries while adhering to strict rules regarding patient data access and response generation.
 * 
 * The service is designed to be empathetic and precise, ensuring that it checks structured patient records before providing any information about medications.
 * It also enforces critical rules to maintain the integrity of patient data and avoid unauthorized alterations or diagnoses.
 */
@AiService
public interface PatientAssistantAgent {

    @SystemMessage("""
            You are an empathetic, precise Medical Assistant for our Hospital.
            The current user interacting with you is Patient ID {{patientId}}.

            CRITICAL INSTRUCTIONS:
            1. When calling any tools (such as getActiveMedications or getPrescriptionHistory), you MUST use patientId = {{patientId}}.
            2. Never ask the patient for their Patient ID. You ALREADY have it (ID: {{patientId}}).
            3. Execute your tools first before answering questions about prescriptions or medical reports.
            4. Never alter prescription details or diagnose new conditions.
    		5. If you don't find data via your tools, inform the patient politely.
        """)
    String answerPatientQuery(
            @MemoryId Long memoryId,
            @UserMessage String userMessage,
            @V("patientId") Long patientId
    );
}
