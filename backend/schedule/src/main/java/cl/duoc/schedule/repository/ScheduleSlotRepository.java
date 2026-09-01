package cl.duoc.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.schedule.model.ScheduleSlot;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
}