package cl.duoc.medical_records.repository;
 

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.medical_records.model.MedicalRecord; 
 


@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long>
{
    public boolean existsByPatientId(Long patientId);
    public Optional<MedicalRecord> findByPatientId(Long patientId);
}
