package cl.duoc.medical_records.service;
 
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO; 
import cl.duoc.medical_records.dto.MedicalVisitDetailReponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.MedicalVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalVisitService 
{
    private final ToDTO toDTO;

    private final MedicalVisitRepository medicalVisitRepository;

    public MedicalVisitResponseDTO create(CreateMedicalVisitRequestDTO requestDTO)
    {
        //Creación y guardado de modelo
        MedicalVisit medicalVisit = toDTO.toMedicalVisit(requestDTO);
        medicalVisitRepository.save(medicalVisit);

        //Creación y retorno de responseDTO
        return toDTO.toMedicalVisitResponseDTO(medicalVisit);
    }

    public MedicalVisitDetailReponseDTO findById(Long id)
    {
        //Validación y asignación de visita médica
        MedicalVisit medicalVisit = medicalVisitRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No existe una visita médica registrada con ese ID"));
        
        //Creación y retorno de responseDTO
        return toDTO.toMedicalVisitDetailReponseDTO(medicalVisit);
    }
}
