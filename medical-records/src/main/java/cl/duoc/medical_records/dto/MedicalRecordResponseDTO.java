package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;

public record MedicalRecordResponseDTO
(
    Long MedicalRecordId,
    Long patientUserId,
    boolean active,
    LocalDateTime createdAT
) {}
