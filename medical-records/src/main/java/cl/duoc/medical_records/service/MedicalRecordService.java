package cl.duoc.medical_records.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateMedicalRecordRequestDTO;
import cl.duoc.medical_records.dto.MedicalRecordDetailResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalRecord; 
import cl.duoc.medical_records.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService 
{
    @Autowired
    private ToDTO toDTO;

    private final MedicalRecordRepository medicalRecordRepository;

    
    public MedicalRecordResponseDTO create(CreateMedicalRecordRequestDTO requestDTO)
    {
        //Validación
        if (!medicalRecordRepository.existsByPatientId(requestDTO.patientId()))
        {
            throw new RuntimeException("Ya existe un registro médico con el ID de ese paciente.");
        }

        //Creación y guardado de modelo
        MedicalRecord medicalRecord = toDTO.toMedicalRecord(requestDTO);
        medicalRecordRepository.save(medicalRecord);

        //Creación y retorno de responseDTO
        return toDTO.toMedicalRecordResponseDTO(medicalRecord);
    }


    public MedicalRecordDetailResponseDTO findByPatientId(Long id)
    {
        //Validación y asignación del registro del paciente
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientId(id)
            .orElseThrow(() -> new RuntimeException("No existe un registro médico asociado al ID del paciente"));

        //Creación y retorno de responseDTO
        return toDTO.toMedicalRecordDetailResponseDTO(medicalRecord);
    }

    public List<MedicalRecordDetailResponseDTO> listAll()
    {
        return medicalRecordRepository.findAll()
            .stream()
            .map(x -> toDTO.toMedicalRecordDetailResponseDTO(x))
            .collect(Collectors.toList());
    }
}
