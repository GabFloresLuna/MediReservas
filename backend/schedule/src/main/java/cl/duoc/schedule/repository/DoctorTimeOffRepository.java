package cl.duoc.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.schedule.model.DoctorTimeOff;

public interface DoctorTimeOffRepository extends JpaRepository<DoctorTimeOff, Long> {
}
