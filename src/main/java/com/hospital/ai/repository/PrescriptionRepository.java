package com.hospital.ai.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.ai.model.Prescription;


public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
		List<Prescription> findByPatientIdAndActiveTrue(Long patientId);

		Collection<Prescription> findByPatientId(Long patientId);

}
