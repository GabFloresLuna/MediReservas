package cl.duoc.schedule.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.schedule.dto.DoctorScheduleRequest;
import cl.duoc.schedule.dto.DoctorScheduleResponse;
import cl.duoc.schedule.model.DoctorSchedule;
import cl.duoc.schedule.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    public DoctorScheduleResponse create(DoctorScheduleRequest request) {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(request.getDoctorId());
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setActive(request.getActive());

        DoctorSchedule saved = doctorScheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    public DoctorScheduleResponse findById(Long id) {
        try {
            DoctorSchedule schedule = doctorScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Horario de doctor no encontrado con ID: " + id));
            return mapToResponse(schedule);
        } catch (RuntimeException ex) {
            log.error("Error finding DoctorSchedule by id: {}", id, ex);
            throw ex;
        }
    }

    public List<DoctorScheduleResponse> findAll() {
        return doctorScheduleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DoctorScheduleResponse update(Long id, DoctorScheduleRequest request) {
        try {
            DoctorSchedule schedule = doctorScheduleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Horario de doctor no encontrado con ID: " + id));

            schedule.setDoctorId(request.getDoctorId());
            schedule.setDayOfWeek(request.getDayOfWeek());
            schedule.setStartTime(request.getStartTime());
            schedule.setEndTime(request.getEndTime());
            schedule.setActive(request.getActive());

            DoctorSchedule updated = doctorScheduleRepository.save(schedule);
            return mapToResponse(updated);
        } catch (RuntimeException ex) {
            log.error("Error updating DoctorSchedule with id: {}", id, ex);
            throw ex;
        }
    }

    public void delete(Long id) {
        try {
            if (!doctorScheduleRepository.existsById(id)) {
                throw new RuntimeException("Horario de doctor no encontrado con ID: " + id);
            }
            doctorScheduleRepository.deleteById(id);
        } catch (RuntimeException ex) {
            log.error("Error deleting DoctorSchedule with id: {}", id, ex);
            throw ex;
        }
    }

    private DoctorScheduleResponse mapToResponse(DoctorSchedule schedule) {
        return new DoctorScheduleResponse(
                schedule.getDoctorScheduleId(),
                schedule.getDoctorId(),
                schedule.getDayOfWeek().name(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getActive()
        );
    }
}