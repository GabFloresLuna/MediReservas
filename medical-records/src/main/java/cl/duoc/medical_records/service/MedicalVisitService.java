package cl.duoc.medical_records.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ToDTO toDTO;

    private final MedicalVisitRepository medicalVisitRepository;

    public MedicalVisitResponseDTO create(CreateMedicalVisitRequestDTO requestDTO)
    {
        //Creación y guardado de modelo
        MedicalVisit medicalVisit = toDTO.toMedicalVisit(requestDTO);
        medicalVisitRepository.save(medicalVisit);

        //Creación y retorno de responseDTO
        return toDTO.toMedicalVisitResponseDTO(medicalVisit);
    }

    public List<MedicalVisitDetailReponseDTO> findAllById(Long id)
    {
        //Validación y asignación de visita médica
        List<MedicalVisit> medicalVisit = medicalVisitRepository.findAllByPatientId(id)
            .stream()
            .map(x -> x.orElseThrow(() -> new RuntimeException("No existe una visita médica registrada con ese ID")))
            .collect(Collectors.toList());
        
        //Creación y retorno de responseDTO
        return medicalVisit.stream().map(x -> toDTO.toMedicalVisitDetailReponseDTO(x)).collect(Collectors.toList());
    }

}
