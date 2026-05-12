package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;

public record MedicalVisitResponseDTO
(
    Long medicalVisitId,
    Long medicalRecordId,
    Long appointmentId,
    Long doctorId,
    LocalDateTime visitDate,
    String visitReason,
    String observations,
    String treatment,
    LocalDateTime createdAt
) {}
