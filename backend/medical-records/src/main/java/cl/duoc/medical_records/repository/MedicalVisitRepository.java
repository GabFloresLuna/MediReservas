package cl.duoc.medical_records.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.medical_records.model.MedicalVisit;

public interface MedicalVisitRepository
        extends JpaRepository<MedicalVisit, Long> {

    List<MedicalVisit> findByMedicalRecordId(Long medicalRecordId);

    List<MedicalVisit> findByMedicalRecordPatientId(Long patientId);

    List<MedicalVisit> findByDoctorId(Long doctorId);

    Optional<MedicalVisit> findByAppointmentId(Long appointmentId);

    boolean existsByAppointmentId(Long appointmentId);

    boolean existsByMedicalRecordId(Long medicalRecordId);
}