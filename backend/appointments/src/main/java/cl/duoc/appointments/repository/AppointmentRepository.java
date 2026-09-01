package cl.duoc.appointments.repository;

import cl.duoc.appointments.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientUserId(Long patientUserId);

    List<Appointment> findByDoctorId(Long doctorId);

}
