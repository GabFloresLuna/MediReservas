package cl.duoc.appointments.controller;

import cl.duoc.appointments.dto.ApiResponse;
import cl.duoc.appointments.dto.AppointmentCancelRequestDTO;
import cl.duoc.appointments.dto.AppointmentCreateRequestDTO;
import cl.duoc.appointments.dto.AppointmentResponseDTO;
import cl.duoc.appointments.dto.AppointmentStatusChangeRequestDTO;
import cl.duoc.appointments.dto.AppointmentUpdateRequestDTO;
import cl.duoc.appointments.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// AppointmentController — Gestión de reservas médicas
// Expone 11 endpoints REST para crear, consultar, actualizar y cambiar el estado de las citas médicas.
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // POST /api/v1/appointments — Crea una nueva cita médica. El estado inicial siempre es PENDING.
    @PostMapping
    @Operation(summary = "Crear una nueva reserva médica",
               description = "Crea una nueva cita médica. El estado inicial siempre es PENDING.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> createAppointment(
            @Valid @RequestBody AppointmentCreateRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Cita creada exitosamente", response));
    }
    

    // GET /api/v1/appointments — Lista todas las citas médicas registradas.
    @GetMapping
    @Operation(summary = "Listar todas las reservas médicas",
               description = "Retorna una lista con todas las citas médicas registradas.")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Citas obtenidas exitosamente", appointments));
    }


    // GET /api/v1/appointments/{appointmentId} — Obtiene una cita médica específica por su ID.
    @GetMapping("/{appointmentId}")
    @Operation(summary = "Obtener una reserva médica por ID",
               description = "Retorna una cita médica específica según su identificador.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> getAppointmentById(
            @PathVariable Long appointmentId) {
        AppointmentResponseDTO response = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cita obtenida exitosamente", response));
    }


    // GET /api/v1/appointments/patient/{patientUserId} — Lista todas las citas asociadas a un paciente.
    @GetMapping("/patient/{patientUserId}")
    @Operation(summary = "Listar reservas de un paciente",
               description = "Retorna todas las citas médicas asociadas a un paciente.")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getByPatientUserId(
            @PathVariable Long patientUserId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getByPatientUserId(patientUserId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Citas del paciente obtenidas exitosamente", appointments));
    }


    // GET /api/v1/appointments/doctor/{doctorId} — Lista todas las citas asociadas a un médico.
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Listar reservas de un médico",
               description = "Retorna todas las citas médicas asociadas a un médico.")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getByDoctorId(
            @PathVariable Long doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getByDoctorId(doctorId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Citas del médico obtenidas exitosamente", appointments));
    }


    // GET /api/v1/appointments/{appointmentId}/exists — Valida si existe una cita. Usado por otros microservicios.
    @GetMapping("/{appointmentId}/exists")
    @Operation(summary = "Validar existencia de una reserva médica",
               description = "Verifica si existe una cita médica con el ID indicado. Usado por otros microservicios.")
    public ResponseEntity<ApiResponse<Boolean>> existsAppointment(
            @PathVariable Long appointmentId) {
        boolean exists = appointmentService.existsAppointment(appointmentId);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Verificación completada", exists));
    }


    // PUT /api/v1/appointments/{appointmentId} — Actualiza los datos de una cita. Solo permitido en estado PENDING.
    @PutMapping("/{appointmentId}")
    @Operation(summary = "Actualizar una reserva médica",
               description = "Actualiza los datos de una cita. Solo permitido si está en estado PENDING.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> updateAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentUpdateRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.updateAppointment(appointmentId, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cita actualizada exitosamente", response));
    }


    // PATCH /api/v1/appointments/{appointmentId}/confirm — Confirma una cita. Cambia estado de PENDING a CONFIRMED.
    @PatchMapping("/{appointmentId}/confirm")
    @Operation(summary = "Confirmar una reserva médica",
               description = "Cambia el estado de una cita de PENDING a CONFIRMED.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> confirmAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentStatusChangeRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.confirmAppointment(appointmentId, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cita confirmada exitosamente", response));
    }


    // PATCH /api/v1/appointments/{appointmentId}/cancel — Cancela una cita en estado PENDING o CONFIRMED.
    @PatchMapping("/{appointmentId}/cancel")
    @Operation(summary = "Cancelar una reserva médica",
               description = "Cancela una cita en estado PENDING o CONFIRMED. Genera registro de cancelación.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> cancelAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentCancelRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.cancelAppointment(appointmentId, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cita cancelada exitosamente", response));
    }


    // PATCH /api/v1/appointments/{appointmentId}/complete — Marca una cita CONFIRMED como completada.
    @PatchMapping("/{appointmentId}/complete")
    @Operation(summary = "Completar una reserva médica",
               description = "Marca una cita confirmada como completada luego de la atención.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> completeAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentStatusChangeRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.completeAppointment(appointmentId, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cita completada exitosamente", response));
    }


    // PATCH /api/v1/appointments/{appointmentId}/no-show — Marca una cita CONFIRMED como NO_SHOW por inasistencia.
    @PatchMapping("/{appointmentId}/no-show")
    @Operation(summary = "Marcar reserva como inasistencia",
               description = "Marca una cita confirmada como NO_SHOW cuando el paciente no se presentó.")
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> markNoShow(
            @PathVariable Long appointmentId,
            @Valid @RequestBody AppointmentStatusChangeRequestDTO dto) {
        AppointmentResponseDTO response = appointmentService.markNoShow(appointmentId, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Cita marcada como inasistencia", response));
    }
}
