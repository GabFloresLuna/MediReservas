package cl.duoc.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository; 

import cl.duoc.medical_records.model.Diagnoses;
 
public interface DiagnosesRepository extends JpaRepository<Diagnoses, Long> {
    List<Diagnoses> findByMedicalVisitId(Long medicalVisitId);
}
