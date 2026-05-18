package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;
import java.util.List;


public record MedicalRecordDetailResponseDTO
(
    Long medicalRecordId,
    Long patientUserId,
    boolean active,
    LocalDateTime createdAt,
    List<MedicalVisitDetailReponseDTO> visits
) {}
