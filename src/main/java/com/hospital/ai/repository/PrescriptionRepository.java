package com.hospital.ai.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospital.ai.model.Prescription;

/**
 * Repository interface for managing Prescription entities.
 * Provides methods to retrieve active prescriptions and historical prescription records for a specific patient.
 */
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
	
		// Corrected: p.isActive matches the Java field name 'isActive'
	    @Query("SELECT p FROM Prescription p WHERE p.patientId = :patientId AND p.isActive = true")
	    List<Prescription> findByPatientIdAndActiveTrue(Long patientId);

		Collection<Prescription> findByPatientId(Long patientId);

}
