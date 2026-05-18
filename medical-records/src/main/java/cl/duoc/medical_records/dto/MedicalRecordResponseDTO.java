package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;

public record MedicalRecordResponseDTO
(
    Long MedicalRecordId,
    Long patientUserId,
    Boolean active,
    LocalDateTime createdAT
) {}
