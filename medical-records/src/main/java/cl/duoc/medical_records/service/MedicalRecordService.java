package cl.duoc.medical_records.service;

import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateMedicalRecordRequestDTO;
import cl.duoc.medical_records.dto.MedicalRecordResponseDTO;
import cl.duoc.medical_records.model.MedicalRecord;
import cl.duoc.medical_records.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService 
{
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord toMedicalRecord(CreateMedicalRecordRequestDTO requestDTO)
    {
        //Crea modelo
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatientId(requestDTO.patientId());
        return medicalRecord;
    }

    public MedicalRecordResponseDTO toMedicalRecordResponseDTO(MedicalRecord medicalRecord)
    {
        //Crea responseDTO
        return new MedicalRecordResponseDTO
        (
            medicalRecord.getId(),
            medicalRecord.getPatientId(),
            medicalRecord.getActive(),
            medicalRecord.getCreateAt()
        );
    }


    public MedicalRecordResponseDTO create(CreateMedicalRecordRequestDTO requestDTO)
    {
        //Validación
        if (medicalRecordRepository.existsByPatientId(requestDTO.patientId()))
        {
            throw new RuntimeException("Ya existe un historial médico con el ID de ese paciente.");
        }

        //Creación y guardado de modelo
        MedicalRecord medicalRecord = toMedicalRecord(requestDTO);
        medicalRecordRepository.save(medicalRecord);

        //Creación y retorno de responseDTO
        return toMedicalRecordResponseDTO(medicalRecord);
    }
}
