package cl.duoc.medical_records.repository;
 

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import cl.duoc.medical_records.model.MedicalVisit;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit,Long>
{ 
    public List<Optional<MedicalVisit>> findAllByPatientId(Long patientId);
}
