package cl.duoc.medical_records.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.medical_records.model.VitalSigns;

@Repository
public interface VitalSignsRepository extends JpaRepository<VitalSigns,Long>{
    List<VitalSigns> findByMedicalVisitId(Long medicalVisitId);
}
