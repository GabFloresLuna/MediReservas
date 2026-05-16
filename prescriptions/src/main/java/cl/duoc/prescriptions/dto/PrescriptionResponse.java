package cl.duoc.prescriptions.dto;

import java.time.LocalDateTime;

public record PrescriptionResponse(
    Long prescriptionId,
    Long medicalVisitId,
    Long patientUserId,
    Long doctorId,
    LocalDateTime issuedAt,
    String prescriptionStatus,
    String notes
) {}