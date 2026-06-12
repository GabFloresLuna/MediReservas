package cl.duoc.medical_records.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.medical_records.model.VitalSigns;

public interface VitalSignsRepository
        extends JpaRepository<VitalSigns, Long> {

    List<VitalSigns> findByMedicalVisitId(Long medicalVisitId);

    boolean existsByMedicalVisitId(Long medicalVisitId);
}