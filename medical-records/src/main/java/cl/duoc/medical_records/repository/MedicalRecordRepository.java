package cl.duoc.medical_records.repository;
 

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; 

import cl.duoc.medical_records.model.MedicalRecord; 
 

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long>
{
    public boolean existsByPatientId(Long patientId);
    public Optional<MedicalRecord> findByPatientId(Long patientId);
}
