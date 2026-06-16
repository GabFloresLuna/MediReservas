package cl.duoc.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.schedule.model.DoctorSchedule;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
}
