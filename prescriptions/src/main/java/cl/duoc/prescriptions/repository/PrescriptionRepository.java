package cl.duoc.prescriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.prescriptions.model.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {}