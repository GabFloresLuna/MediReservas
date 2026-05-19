package cl.duoc.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.schedule.model.ScheduleSlot;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
}