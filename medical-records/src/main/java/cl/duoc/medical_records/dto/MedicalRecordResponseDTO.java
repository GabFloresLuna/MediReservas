package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MedicalRecordResponseDTO
(
    Long MedicalRecordId,
    Long patientUserId,
    Boolean active,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAT
) {}
