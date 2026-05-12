package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MedicalVisitDetailDTO
(
    Long medicalVisitId,
    Long appointmentId,
    Long doctorId,
    LocalDateTime visitDate,
    String visitReason,
    String observations,
    String treatment,
    LocalDateTime createdAt,
    List<DiagnosisResponseDTO> diagnoses,
    List<VitalSignResponseDTO> vitalSigns
) {}
