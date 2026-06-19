package cl.duoc.schedule.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.schedule.dto.DoctorTimeOffRequest;
import cl.duoc.schedule.dto.DoctorTimeOffResponse;
import cl.duoc.schedule.model.DoctorTimeOff;
import cl.duoc.schedule.repository.DoctorTimeOffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorTimeOffService {

    private final DoctorTimeOffRepository doctorTimeOffRepository;

    public DoctorTimeOffResponse create(DoctorTimeOffRequest request) {
        DoctorTimeOff timeOff = new DoctorTimeOff();
        timeOff.setDoctorId(request.getDoctorId());
        timeOff.setStartDate(request.getStartDate());
        timeOff.setEndDate(request.getEndDate());
        timeOff.setReason(request.getReason());
        timeOff.setActive(request.getActive());
        timeOff.setCreatedAt(LocalDateTime.now());

        DoctorTimeOff saved = doctorTimeOffRepository.save(timeOff);
        return mapToResponse(saved);
    }

    public DoctorTimeOffResponse findById(Long id) {
        try {
            DoctorTimeOff timeOff = doctorTimeOffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Time off no encontrado con ID: " + id));
            return mapToResponse(timeOff);
        } catch (RuntimeException ex) {
            log.error("Error finding DoctorTimeOff by id: {}", id, ex);
            throw ex;
        }
    }

    public List<DoctorTimeOffResponse> findAll() {
        return doctorTimeOffRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DoctorTimeOffResponse update(Long id, DoctorTimeOffRequest request) {
        try {
            DoctorTimeOff timeOff = doctorTimeOffRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Time off no encontrado con ID: " + id));

            timeOff.setDoctorId(request.getDoctorId());
            timeOff.setStartDate(request.getStartDate());
            timeOff.setEndDate(request.getEndDate());
            timeOff.setReason(request.getReason());
            timeOff.setActive(request.getActive());

            DoctorTimeOff updated = doctorTimeOffRepository.save(timeOff);
            return mapToResponse(updated);
        } catch (RuntimeException ex) {
            log.error("Error updating DoctorTimeOff with id: {}", id, ex);
            throw ex;
        }
    }

    public void delete(Long id) {
        try {
            if (!doctorTimeOffRepository.existsById(id)) {
                throw new RuntimeException("Time off no encontrado con ID: " + id);
            }
            doctorTimeOffRepository.deleteById(id);
        } catch (RuntimeException ex) {
            log.error("Error deleting DoctorTimeOff with id: {}", id, ex);
            throw ex;
        }
    }

    private DoctorTimeOffResponse mapToResponse(DoctorTimeOff timeOff) {
        return new DoctorTimeOffResponse(
                timeOff.getDoctorTimeOffId(),
                timeOff.getDoctorId(),
                timeOff.getStartDate(),
                timeOff.getEndDate(),
                timeOff.getReason(),
                timeOff.getActive(),
                timeOff.getCreatedAt()
        );
    }
}