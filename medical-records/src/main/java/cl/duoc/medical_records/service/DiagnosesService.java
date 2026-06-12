package cl.duoc.medical_records.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateDiagnosisRequestDTO;
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.dto.UpdateDiagnosisRequestDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.Diagnoses;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.DiagnosesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosesService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosesService.class);

    private final DiagnosesRepository diagnosesRepository;
    private final MedicalVisitService medicalVisitService;
    private final ToDTO toDTO;

    public DiagnosisResponseDTO create(
            CreateDiagnosisRequestDTO requestDTO) {

        MedicalVisit medicalVisit = medicalVisitService.findMedicalVisitEntityById(
                requestDTO.medicalVisitId());

        Diagnoses diagnosis = toDTO.toDiagnoses(requestDTO);

        diagnosis.setMedicalVisit(medicalVisit);

        Diagnoses saved = diagnosesRepository.save(diagnosis);

        return toDTO.toDiagnosisResponseDTO(saved);
    }

    public DiagnosisResponseDTO findById(Long diagnosisId) {
        Diagnoses diagnosis = findDiagnosisEntityById(diagnosisId);

        return toDTO.toDiagnosisResponseDTO(diagnosis);
    }

    public List<DiagnosisResponseDTO> findByMedicalVisitId(
            Long medicalVisitId) {

        medicalVisitService.findMedicalVisitEntityById(
                medicalVisitId);

        return diagnosesRepository
                .findByMedicalVisitId(medicalVisitId)
                .stream()
                .map(toDTO::toDiagnosisResponseDTO)
                .toList();
    }

    public DiagnosisResponseDTO update(
            Long diagnosisId,
            UpdateDiagnosisRequestDTO requestDTO) {

        Diagnoses diagnosis = findDiagnosisEntityById(diagnosisId);

        if (requestDTO.diagnosisDescription() != null) {
            diagnosis.setDiagnosisDescription(
                    requestDTO.diagnosisDescription());
        }

        if (requestDTO.diagnosisNotes() != null) {
            diagnosis.setDiagnosisNotes(
                    requestDTO.diagnosisNotes());
        }

        Diagnoses updated = diagnosesRepository.save(diagnosis);

        return toDTO.toDiagnosisResponseDTO(updated);
    }

    public void delete(Long diagnosisId) {
        Diagnoses diagnosis = findDiagnosisEntityById(diagnosisId);

        diagnosesRepository.delete(diagnosis);
    }

    public Diagnoses findDiagnosisEntityById(
            Long diagnosisId) {

        return diagnosesRepository.findById(diagnosisId)
                .orElseThrow(() -> {
                    logger.warn(
                            "Diagnóstico no encontrado con ID {}",
                            diagnosisId);

                    return new RuntimeException(
                            "Diagnóstico no encontrado con ID: "
                                    + diagnosisId);
                });
    }
}