package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MedicalVisitResponseDTO
(
    Long medicalVisitId,
    Long medicalRecordId,
    Long appointmentId,
    Long doctorId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime visitDate,
    String visitReason,
    String observations,
    String treatment,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {}
