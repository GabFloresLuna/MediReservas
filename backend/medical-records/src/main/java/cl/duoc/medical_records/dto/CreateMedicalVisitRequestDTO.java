package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public record CreateMedicalVisitRequestDTO
(
    @NotNull(message = "El ID de la ficha médica es obligatorio")
    Long medicalRecordId,

    @NotNull(message = "EL ID de la reserva medica es obligatorio")
    Long appointmentId,

    @NotNull(message = "El ID del doctor es obligatorio")
    Long doctorId,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "La fecha de atención es obligatoria")
    @PastOrPresent(message = "La fecha de atención no puede ser futura")
    LocalDateTime visitDate,

    @NotBlank(message = "El motivo de atención es obligatorio")
    @Size(max = 255, message = "El motivo atención no puede superar los 255 caracteres")
    String visitReason,

    @Size(max = 2000, message = "Las observaciones no pueden superar los 2000 caracteres")
    String observations,

    @NotBlank(message = "El tratamiento es obligatorio")
    @Size(max = 2000, message = "El tratamiento no puede superar los 2000 caracteres")
    String treatment
) {}
