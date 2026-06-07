package cl.duoc.appointments.repository;

import cl.duoc.appointments.model.AppointmentCancellation;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AppointmentCancellationRepository extends JpaRepository<AppointmentCancellation, Long> {

   
}
