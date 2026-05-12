package cl.duoc.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.medical_records.model.Diagnoses;

@Repository
public interface DiagnosesRepository extends JpaRepository<Diagnoses, Long> {

}
