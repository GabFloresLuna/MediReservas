package cl.duoc.schedule.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.schedule.dto.DoctorTimeOffRequest;
import cl.duoc.schedule.dto.DoctorTimeOffResponse;
import cl.duoc.schedule.model.DoctorTimeOff;
import cl.duoc.schedule.repository.DoctorTimeOffRepository;
import lombok.RequiredArgsConstructor;

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
        DoctorTimeOff timeOff = doctorTimeOffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time off no encontrado con ID: " + id));
        return mapToResponse(timeOff);
    }

    public List<DoctorTimeOffResponse> findAll() {
        return doctorTimeOffRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DoctorTimeOffResponse update(Long id, DoctorTimeOffRequest request) {
        DoctorTimeOff timeOff = doctorTimeOffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time off no encontrado con ID: " + id));

        timeOff.setDoctorId(request.getDoctorId());
        timeOff.setStartDate(request.getStartDate());
        timeOff.setEndDate(request.getEndDate());
        timeOff.setReason(request.getReason());
        timeOff.setActive(request.getActive());

        DoctorTimeOff updated = doctorTimeOffRepository.save(timeOff);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        if (!doctorTimeOffRepository.existsById(id)) {
            throw new RuntimeException("Time off no encontrado con ID: " + id);
        }
        doctorTimeOffRepository.deleteById(id);
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