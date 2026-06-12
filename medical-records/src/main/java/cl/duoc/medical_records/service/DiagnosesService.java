package cl.duoc.medical_records.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.Diagnoses;
import cl.duoc.medical_records.repository.DiagnosesRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosesService {
    
    private final ToDTO toDTO;
    private final DiagnosesRepository diagnosesRepository;

    public DiagnosisResponseDTO create(CreateDiagnosisRequestDTO requestDTO) {
        Diagnoses diagnoses = toDTO.toDiagnoses(requestDTO);
        Diagnoses saved = diagnosesRepository.save(diagnoses);
        return toDTO.toDiagnosisResponseDTO(saved);
    }

    public DiagnosisResponseDTO findById(Long id) {
        Diagnoses diagnoses = diagnosesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con ID: " + id));
        return toDTO.toDiagnosisResponseDTO(diagnoses);
    }

    public List<DiagnosisResponseDTO> findByMedicalVisitId(Long medicalVisitId) {
        return diagnosesRepository.findByMedicalVisitId(medicalVisitId)
            .stream()
            .map(toDTO::toDiagnosisResponseDTO)
            .collect(Collectors.toList());
    }

    public DiagnosisResponseDTO update(Long id, UpdateDiagnosisRequestDTO requestDTO) {
        Diagnoses existingDiagnosis = diagnosesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Diagnóstico no encontrado con ID: " + id));
        
        if (requestDTO.diagnosisDescription() != null) {
            existingDiagnosis.setDiagnosisDescription(requestDTO.diagnosisDescription());
        }
        if (requestDTO.diagnosisNotes() != null) {
            existingDiagnosis.setDiagnosisNotes(requestDTO.diagnosisNotes());
        }
        
        Diagnoses updated = diagnosesRepository.save(existingDiagnosis);
        return toDTO.toDiagnosisResponseDTO(updated);
    }

    public void delete(Long id) {
        if (!diagnosesRepository.existsById(id)) {
            throw new RuntimeException("Diagnóstico no encontrado con ID: " + id);
        }
        diagnosesRepository.deleteById(id);
    }
}