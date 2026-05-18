package cl.duoc.medical_records.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDiagnosisRequestDTO
(
    @NotBlank(message = "La descripción del diagnóstico es obligatoria")
    @Size(max = 255, message = "La descripción del diagnóstico no puede superar los 255 caracteres")
    String diagnosisDescription,

    @Size(max = 2000, message = "Las notas del diagnóstico no pueden superar los 2000 caracteres")
    String diagnosisNotes
) {}
