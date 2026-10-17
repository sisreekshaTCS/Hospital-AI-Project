package com.hospital.ai.tools;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.ai.model.Prescription;
import com.hospital.ai.model.PrescriptionDto;
import com.hospital.ai.repository.PrescriptionRepository;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

@Service
public class PatientRecordTools {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Tool("Retrieves the list of active prescribed medications, including dosage and frequency instructions, for a specific patient ID")
    public List<PrescriptionDto> getActiveMedications(
            @P("The unique numeric ID of the patient") Long patientId) {
        System.out.println("--> [TOOL CALLED]: Executing SQL query for patient ID: " + patientId);

        List<Prescription> activePrescriptions = prescriptionRepo.findByPatientIdAndActiveTrue(patientId);

        return activePrescriptions.stream()
                .map(p -> new PrescriptionDto(
                		p.getId(),
                		p.getPatientId(),
                        p.getMedicationName(),
                        p.getDosage(),
                        p.getFrequency(),
                        p.getPrescribedDate(),
                        p.isActive() ? "Active" : "Discontinued"
                ))
                .collect(Collectors.toList());
    }
    
    @Tool("Retrieves historical past prescriptions that are no longer active for a specific patient ID")
    public List<PrescriptionDto> getPrescriptionHistory(
            @P("The unique numeric ID of the patient") Long patientId) {
        
        return prescriptionRepo.findByPatientId(patientId).stream()
                .filter(p -> !p.isActive())
                .map(p -> new PrescriptionDto(
                		p.getId(),
                		p.getPatientId(),
                        p.getMedicationName(),
                        p.getDosage(),
                        p.getFrequency(),
                        p.getPrescribedDate(),
                        "Discontinued"
                ))
                .collect(Collectors.toList());
    }
}
