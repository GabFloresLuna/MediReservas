package cl.duoc.medical_records.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateDiagnosisRequestDTO; 
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.Diagnoses;
import cl.duoc.medical_records.repository.DiagnosesRepository; 
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosesService 
{
    @Autowired
    private ToDTO toDTO;

    private final DiagnosesRepository diagnosesRepository;

    public DiagnosisResponseDTO create(CreateDiagnosisRequestDTO requestDTO)
    {
        //Creación y guardado de modelo
        Diagnoses diagnoses = toDTO.toDiagnoses(requestDTO);
        diagnosesRepository.save(diagnoses);

        //Creación y retorno de responseDTO
        return toDTO.toDiagnosisResponseDTO(diagnoses);
    }

}
