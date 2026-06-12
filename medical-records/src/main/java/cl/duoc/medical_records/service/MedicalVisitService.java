package cl.duoc.medical_records.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.MedicalVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalVisitService {
    
    private final ToDTO toDTO;
    private final MedicalVisitRepository medicalVisitRepository;

    public MedicalVisitResponseDTO create(CreateMedicalVisitRequestDTO requestDTO) {
        MedicalVisit medicalVisit = toDTO.toMedicalVisit(requestDTO);
        MedicalVisit saved = medicalVisitRepository.save(medicalVisit);
        return toDTO.toMedicalVisitResponseDTO(saved);
    }

    public List<MedicalVisitDetailReponseDTO> findAllByPatientId(Long patientId) {
        List<MedicalVisit> medicalVisits = medicalVisitRepository.findByMedicalRecord_PatientId(patientId);
        
        if (medicalVisits.isEmpty()) {
            throw new RuntimeException("No existen visitas médicas para el paciente con ID: " + patientId);
        }
        
        return medicalVisits.stream()
            .map(toDTO::toMedicalVisitDetailReponseDTO)
            .collect(Collectors.toList());
    }

    public MedicalVisitDetailReponseDTO findDetailById(Long medicalVisitId) {
        MedicalVisit medicalVisit = medicalVisitRepository.findById(medicalVisitId)
            .orElseThrow(() -> new RuntimeException("Visita médica no encontrada con ID: " + medicalVisitId));
        return toDTO.toMedicalVisitDetailReponseDTO(medicalVisit);
    }

    public List<MedicalVisitResponseDTO> findByMedicalRecordId(Long medicalRecordId) {
        return medicalVisitRepository.findByMedicalRecordId(medicalRecordId)
            .stream()
            .map(toDTO::toMedicalVisitResponseDTO)
            .collect(Collectors.toList());
    }

    public MedicalVisitResponseDTO update(Long medicalVisitId, UpdateMedicalVisitRequestDTO requestDTO) {
        MedicalVisit existingVisit = medicalVisitRepository.findById(medicalVisitId)
            .orElseThrow(() -> new RuntimeException("Visita médica no encontrada con ID: " + medicalVisitId));
        
        if (requestDTO.visitReason() != null) {
            existingVisit.setVisitReason(requestDTO.visitReason());
        }
        if (requestDTO.observations() != null) {
            existingVisit.setObservations(requestDTO.observations());
        }
        if (requestDTO.treatment() != null) {
            existingVisit.setTreatment(requestDTO.treatment());
        }
        
        MedicalVisit updated = medicalVisitRepository.save(existingVisit);
        return toDTO.toMedicalVisitResponseDTO(updated);
    }

    public void delete(Long medicalVisitId) {
        MedicalVisit medicalVisit = medicalVisitRepository.findById(medicalVisitId)
            .orElseThrow(() -> new RuntimeException("Visita médica no encontrada con ID: " + medicalVisitId));
        
        if ((medicalVisit.getDiagnoses() != null && !medicalVisit.getDiagnoses().isEmpty()) ||
            (medicalVisit.getVitalSigns() != null && !medicalVisit.getVitalSigns().isEmpty())) {
            throw new IllegalStateException("No se puede eliminar la visita médica porque tiene diagnósticos o signos vitales asociados");
        }
        
        medicalVisitRepository.deleteById(medicalVisitId);
    }
}