package cl.duoc.schedule.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.schedule.dto.ScheduleSlotRequest;
import cl.duoc.schedule.dto.ScheduleSlotResponse;
import cl.duoc.schedule.model.ScheduleSlot;
import cl.duoc.schedule.repository.ScheduleSlotRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleSlotService {

    private final ScheduleSlotRepository scheduleSlotRepository;

    public ScheduleSlotResponse create(ScheduleSlotRequest request) {
        ScheduleSlot slot = new ScheduleSlot();
        slot.setDoctorId(request.getDoctorId());
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setSlotStatus(request.getSlotStatus());
        slot.setAppointmentId(request.getAppointmentId());
        slot.setCreatedAt(LocalDateTime.now());

        ScheduleSlot saved = scheduleSlotRepository.save(slot);
        return mapToResponse(saved);
    }

    public ScheduleSlotResponse findById(Long id) {
        ScheduleSlot slot = scheduleSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado con ID: " + id));
        return mapToResponse(slot);
    }

    public List<ScheduleSlotResponse> findAll() {
        return scheduleSlotRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ScheduleSlotResponse update(Long id, ScheduleSlotRequest request) {
        ScheduleSlot slot = scheduleSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot no encontrado con ID: " + id));

        slot.setDoctorId(request.getDoctorId());
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setSlotStatus(request.getSlotStatus());
        slot.setAppointmentId(request.getAppointmentId());

        ScheduleSlot updated = scheduleSlotRepository.save(slot);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        if (!scheduleSlotRepository.existsById(id)) {
            throw new RuntimeException("Slot no encontrado con ID: " + id);
        }
        scheduleSlotRepository.deleteById(id);
    }

    private ScheduleSlotResponse mapToResponse(ScheduleSlot slot) {
        return new ScheduleSlotResponse(
                slot.getScheduleSlotId(),
                slot.getDoctorId(),
                slot.getSlotDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.getSlotStatus().name(),
                slot.getAppointmentId(),
                slot.getCreatedAt()
        );
    }
}