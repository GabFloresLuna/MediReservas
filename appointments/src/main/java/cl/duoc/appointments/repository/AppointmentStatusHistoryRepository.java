package cl.duoc.appointments.repository;

import cl.duoc.appointments.model.AppointmentStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentStatusHistoryRepository extends JpaRepository<AppointmentStatusHistory, Long> {


}
