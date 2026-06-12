package cl.duoc.medical_records.repository;
 

import org.springframework.data.jpa.repository.JpaRepository; 
 
import cl.duoc.medical_records.model.MedicalVisit;
 
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit,Long>
{ 
}
