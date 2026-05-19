package cl.duoc.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.schedule.model.DoctorTimeOff;

@Repository
public interface DoctorTimeOffRepository extends JpaRepository<DoctorTimeOff, Long> {
}
