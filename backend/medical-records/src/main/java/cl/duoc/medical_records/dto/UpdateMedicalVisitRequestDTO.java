package cl.duoc.medical_records.dto;

import jakarta.validation.constraints.Size;

public record UpdateMedicalVisitRequestDTO
(
    @Size(max = 255, message = "El motivo de atención no puede superar los 255 caracteres")
    String visitReason,

    @Size(max = 2000, message = "Las observaciones no pueden superar los 2000 caracteres")
    String observations,

    @Size(max = 2000, message = "El tratamiento no puede superar los 2000 caracteres")
    String treatment
) {}
