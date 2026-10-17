package com.hospital.ai.model;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for Prescription entity.
 * This record is used to transfer prescription data between layers of the application.
 */
public record PrescriptionDto(
        Long id,
        Long patientId,
        String medicationName,
        String dosage,
        String frequency,
        LocalDate prescribedDate,
        String isActive) {}
