package cl.duoc.medical_records.dto;

import jakarta.validation.constraints.NotNull;

public record CreateMedicalRecordRequestDTO
(
    @NotNull(message = "El ID del paciente es obligatorio")
    Long patientUserId
) {}
