package cl.duoc.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository; 

import cl.duoc.medical_records.model.VitalSigns;
 
public interface VitalSignsRepository extends JpaRepository<VitalSigns,Long>{

}
