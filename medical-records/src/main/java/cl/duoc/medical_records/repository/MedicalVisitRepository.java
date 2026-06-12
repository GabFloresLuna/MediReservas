package cl.duoc.medical_records.repository;
 

import java.util.List; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import cl.duoc.medical_records.model.MedicalVisit;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
    List<MedicalVisit> findByMedicalRecord_PatientId(Long patientId);
    List<MedicalVisit> findByMedicalRecordId(Long medicalRecordId);
}