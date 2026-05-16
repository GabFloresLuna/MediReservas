package cl.duoc.medical_records.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateVitalSignRequestDTO;
import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.VitalSigns;
import cl.duoc.medical_records.repository.VitalSignsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VitalSignsService 
{
    @Autowired
    private ToDTO toDTO;

    private VitalSignsRepository vitalSignsRepository;


    public VitalSignResponseDTO create(CreateVitalSignRequestDTO requestDTO)
    {
        //Creación y guardado de modelo
        VitalSigns vitalSigns = toDTO.toVitalSigns(requestDTO);
        vitalSignsRepository.save(vitalSigns);

        //Creación y retorno de responseDTO
        return toDTO.toVitalSignResponseDTO(vitalSigns);
    }
}
