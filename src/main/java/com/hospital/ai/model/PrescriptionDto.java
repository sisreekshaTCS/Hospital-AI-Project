package com.hospital.ai.model;

import java.time.LocalDate;

public record PrescriptionDto(
        Long id,
        Long patientId,
        String medicationName,
        String dosage,
        String frequency,
        LocalDate prescribedDate,
        String isActive) {}
