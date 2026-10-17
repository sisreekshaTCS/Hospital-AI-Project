package com.hospital.ai.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a prescription record for a patient.
 * 
 * This entity is mapped to the "prescriptions" table in the database.
 * It contains information about the prescribed medication, dosage, frequency,
 * and the date it was prescribed. The "isActive" field indicates whether
 * the prescription is currently active or has been discontinued.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prescriptions")
public class Prescription {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "medication_name", nullable = false)
    private String medicationName;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "prescribed_date")
    private LocalDate prescribedDate;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

}
