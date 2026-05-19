package cl.duoc.schedule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.schedule.dto.ApiResponse;
import cl.duoc.schedule.dto.DoctorScheduleRequest;
import cl.duoc.schedule.dto.DoctorScheduleResponse;
import cl.duoc.schedule.services.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/doctor-schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> create(@Valid @RequestBody DoctorScheduleRequest request) {
        DoctorScheduleResponse data = doctorScheduleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Horario creado", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> findById(@PathVariable Long id) {
        DoctorScheduleResponse data = doctorScheduleService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Horario encontrado", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponse>>> findAll() {
        List<DoctorScheduleResponse> data = doctorScheduleService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lista de horarios", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> update(@PathVariable Long id, @Valid @RequestBody DoctorScheduleRequest request) {
        DoctorScheduleResponse data = doctorScheduleService.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Horario actualizado", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        doctorScheduleService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Horario eliminado", null));
    }
}
