package cl.duoc.medical_records.extras;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cl.duoc.medical_records.dto.CreateDiagnosisRequestDTO;
import cl.duoc.medical_records.dto.CreateMedicalRecordRequestDTO;
import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO;
import cl.duoc.medical_records.dto.CreateVitalSignRequestDTO;
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordDetailResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordResponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitDetailReponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO;
import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.model.Diagnoses;
import cl.duoc.medical_records.model.MedicalRecord;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.model.VitalSigns;
import cl.duoc.medical_records.repository.MedicalRecordRepository;
import cl.duoc.medical_records.repository.MedicalVisitRepository;

@Component
public class ToDTO 
{
    private MedicalRecordRepository medicalRecordRepository;
    private MedicalVisitRepository medicalVisitRepository;

    public MedicalRecord toMedicalRecord(CreateMedicalRecordRequestDTO requestDTO)
    {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatientId(requestDTO.patientId());
        return medicalRecord;
    }

    public MedicalRecordResponseDTO toMedicalRecordResponseDTO(MedicalRecord medicalRecord)
    {
        return new MedicalRecordResponseDTO
        (
            medicalRecord.getId(),
            medicalRecord.getPatientId(),
            medicalRecord.getActive(),
            medicalRecord.getCreateAt()
        );
    }

    public MedicalVisit toMedicalVisit(CreateMedicalVisitRequestDTO requestDTO)
    {
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

    public VitalSigns toVitalSigns(CreateVitalSignRequestDTO requestDTO)
    {
        VitalSigns vitalSigns = new VitalSigns();
        vitalSigns.setMedicalVisit(medicalVisitRepository
            .findById(requestDTO.medicalVisitId())
            .orElseThrow(() -> new RuntimeException("No se ha encontrado una visita médica asociada a ese ID")));
        vitalSigns.setTemperature(requestDTO.temperature());
        vitalSigns.setBloodPressure(requestDTO.bloodPressure());
        vitalSigns.setHeartRate(requestDTO.heartRate());
        vitalSigns.setWeight(requestDTO.weight());
        vitalSigns.setHeight(requestDTO.height());
        return vitalSigns;
    }

    public VitalSignResponseDTO toVitalSignResponseDTO(VitalSigns vitalSigns)
    {
        return new VitalSignResponseDTO
        (
            vitalSigns.getId(),
            vitalSigns.getMedicalVisit().getId(),
            vitalSigns.getTemperature(),
            vitalSigns.getBloodPressure(),
            vitalSigns.getHeartRate(),
            vitalSigns.getWeight(),
            vitalSigns.getHeight(),
            vitalSigns.getCreatedAt()
        );
    }

    public MedicalVisitDetailReponseDTO toMedicalVisitDetailReponseDTO(MedicalVisit medicalVisit)
    {
        return new MedicalVisitDetailReponseDTO
        (
            medicalVisit.getId(),
            medicalVisit.getAppointmentId(),
            medicalVisit.getDoctorId(),
            medicalVisit.getVisitDate(),
            medicalVisit.getVisitReason(),
            medicalVisit.getObservations(),
            medicalVisit.getTreatment(),
            medicalVisit.getCreateAt(),
            medicalVisit.getDiagnoses().stream()
                .map(this::toDiagnosisResponseDTO)
                .collect(Collectors.toList()),
            medicalVisit.getVitalSigns().stream()
                .map(this::toVitalSignResponseDTO)
                .collect(Collectors.toList())
        );
    }

    public MedicalRecordDetailResponseDTO toMedicalRecordDetailResponseDTO(MedicalRecord medicalRecord)
    {
        return new MedicalRecordDetailResponseDTO
        (
            medicalRecord.getId(),
            medicalRecord.getPatientId(),
            medicalRecord.getActive(),
            medicalRecord.getCreateAt(),
            medicalRecord.getMedicalVisits().stream()
                .map(this::toMedicalVisitDetailReponseDTO)
                .collect(Collectors.toList())
        );
    }
}
