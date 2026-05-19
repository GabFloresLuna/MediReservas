package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record MedicalVisitDetailReponseDTO
(
    Long medicalVisitId,
    Long appointmentId,
    Long doctorId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime visitDate,
    String visitReason,
    String observations,
    String treatment,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    List<DiagnosisResponseDTO> diagnoses,
    List<VitalSignResponseDTO> vitalSigns
) {}
