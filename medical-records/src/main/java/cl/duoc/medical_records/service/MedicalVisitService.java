package cl.duoc.medical_records.service;

import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO;
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO; 
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.MedicalRecordRepository;
import cl.duoc.medical_records.repository.MedicalVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalVisitService 
{
    private MedicalVisitRepository medicalVisitRepository;
    private MedicalRecordRepository medicalRecordRepository;

    public MedicalVisit toMedicalVisit(CreateMedicalVisitRequestDTO requestDTO)
    {
        //Crea modelo
        MedicalVisit medicalVisit = new MedicalVisit();
        medicalVisit.setMedicalRecord(medicalRecordRepository
            .findById(requestDTO.medicalRecordId())
            .orElseThrow(() -> new RuntimeException("No se ha encontrado un registro médico asociado a ese ID.")));
        medicalVisit.setAppointmentId(requestDTO.appointmentId());
        medicalVisit.setDoctorId(requestDTO.doctorId());
        medicalVisit.setVisitDate(requestDTO.visitDate());
        medicalVisit.setVisitReason(requestDTO.visitReason());
        medicalVisit.setObservations(requestDTO.observations());
        medicalVisit.setTreatment(requestDTO.treatment());
        return medicalVisit;
    }

    public MedicalVisitResponseDTO toMedicalVisitResponseDTO(MedicalVisit medicalVisit)
    {
        //Crea responseDTO   
        return new MedicalVisitResponseDTO
        (
            medicalVisit.getId(),
            medicalVisit.getMedicalRecord().getId(),
            medicalVisit.getAppointmentId(),
            medicalVisit.getDoctorId(),
            medicalVisit.getVisitDate(),
            medicalVisit.getVisitReason(),
            medicalVisit.getObservations(),
            medicalVisit.getTreatment(),
            medicalVisit.getCreateAt()
        );
    }


    public MedicalVisitResponseDTO create(CreateMedicalVisitRequestDTO requestDTO)
    {
        //Creación y guardado de modelo
        MedicalVisit medicalVisit = toMedicalVisit(requestDTO);
        medicalVisitRepository.save(medicalVisit);

        //Creación y retorno de responseDTO
        return toMedicalVisitResponseDTO(medicalVisit);
    }
}
