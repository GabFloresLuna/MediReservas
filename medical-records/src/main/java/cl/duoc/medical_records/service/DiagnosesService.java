package cl.duoc.medical_records.service;

import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateDiagnosisRequestDTO; 
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.model.Diagnoses;
import cl.duoc.medical_records.repository.DiagnosesRepository;
import cl.duoc.medical_records.repository.MedicalVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosesService 
{
    private DiagnosesRepository diagnosesRepository;
    private MedicalVisitRepository medicalVisitRepository;

    public Diagnoses toDiagnoses(CreateDiagnosisRequestDTO requestDTO)
    {
        Diagnoses diagnoses = new Diagnoses();
        diagnoses.setMedicalVisit(medicalVisitRepository
            .findById(requestDTO.medicalVisitId())
            .orElseThrow(() -> new RuntimeException("No se ha encontrado una visita médica asociada a ese ID.")));
        diagnoses.setDiagnosisDescription(requestDTO.diagnosisDescription());
        diagnoses.setDiagnosisNotes(requestDTO.diagnosisNotes());
        return diagnoses;
    }

    public DiagnosisResponseDTO toDiagnosisResponseDTO (Diagnoses diagnoses)
    {
        return new DiagnosisResponseDTO
        (
            diagnoses.getId(),
            diagnoses.getMedicalVisit().getId(),
            diagnoses.getDiagnosisDescription(),
            diagnoses.getDiagnosisNotes()
        );
    }

    public DiagnosisResponseDTO create(CreateDiagnosisRequestDTO requestDTO)
    {
        //Creación y guardado de modelo
        Diagnoses diagnoses = toDiagnoses(requestDTO);
        diagnosesRepository.save(diagnoses);

        //Creación y retorno de responseDTO
        return toDiagnosisResponseDTO(diagnoses);
    }
}
