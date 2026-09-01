package cl.duoc.medical_records.dto;

public record DiagnosisResponseDTO
(
    Long diagnosisId,
    Long medicalVisitId,
    String diagnosisDescription,
    String diagnosisNotes
) {}
