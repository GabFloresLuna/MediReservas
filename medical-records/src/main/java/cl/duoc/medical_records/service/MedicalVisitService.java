package cl.duoc.medical_records.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO;
import cl.duoc.medical_records.dto.MedicalVisitDetailReponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO;
import cl.duoc.medical_records.dto.UpdateMedicalVisitRequestDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalRecord;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.DiagnosesRepository;
import cl.duoc.medical_records.repository.MedicalVisitRepository;
import cl.duoc.medical_records.repository.VitalSignsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalVisitService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalVisitService.class);

    private final MedicalVisitRepository medicalVisitRepository;
    private final DiagnosesRepository diagnosesRepository;
    private final VitalSignsRepository vitalSignsRepository;
    private final MedicalRecordService medicalRecordService;
    private final ToDTO toDTO;

    public MedicalVisitResponseDTO create(
            CreateMedicalVisitRequestDTO requestDTO) {

        if (medicalVisitRepository.existsByAppointmentId(
                requestDTO.appointmentId())) {

            logger.warn(
                    "Creación de visita rechazada: appointmentId ya utilizado. appointmentId={}",
                    requestDTO.appointmentId());

            throw new RuntimeException(
                    "Ya existe una visita médica asociada a esa reserva");
        }

        MedicalRecord medicalRecord = medicalRecordService.findMedicalRecordEntityById(
                requestDTO.medicalRecordId());

        if (Boolean.FALSE.equals(medicalRecord.getActive())) {
            throw new RuntimeException(
                    "No se puede crear una visita en un registro médico inactivo");
        }

        MedicalVisit medicalVisit = toDTO.toMedicalVisit(requestDTO);

        medicalVisit.setMedicalRecord(medicalRecord);

        MedicalVisit saved = medicalVisitRepository.save(medicalVisit);

        return toDTO.toMedicalVisitResponseDTO(saved);
    }

    public List<MedicalVisitDetailReponseDTO> findAllByPatientId(
            Long patientId) {

        return medicalVisitRepository
                .findByMedicalRecordPatientId(patientId)
                .stream()
                .map(toDTO::toMedicalVisitDetailReponseDTO)
                .toList();
    }

    public MedicalVisitDetailReponseDTO findDetailById(
            Long medicalVisitId) {

        MedicalVisit medicalVisit = findMedicalVisitEntityById(medicalVisitId);

        return toDTO.toMedicalVisitDetailReponseDTO(
                medicalVisit);
    }

    public List<MedicalVisitResponseDTO> findByMedicalRecordId(
            Long medicalRecordId) {

        medicalRecordService.findMedicalRecordEntityById(
                medicalRecordId);

        return medicalVisitRepository
                .findByMedicalRecordId(medicalRecordId)
                .stream()
                .map(toDTO::toMedicalVisitResponseDTO)
                .toList();
    }

    public MedicalVisitResponseDTO update(
            Long medicalVisitId,
            UpdateMedicalVisitRequestDTO requestDTO) {

        MedicalVisit medicalVisit = findMedicalVisitEntityById(medicalVisitId);

        if (requestDTO.visitReason() != null) {
            medicalVisit.setVisitReason(
                    requestDTO.visitReason());
        }

        if (requestDTO.observations() != null) {
            medicalVisit.setObservations(
                    requestDTO.observations());
        }

        if (requestDTO.treatment() != null) {
            medicalVisit.setTreatment(
                    requestDTO.treatment());
        }

        MedicalVisit updated = medicalVisitRepository.save(medicalVisit);

        return toDTO.toMedicalVisitResponseDTO(updated);
    }

    public void delete(Long medicalVisitId) {
        MedicalVisit medicalVisit = findMedicalVisitEntityById(medicalVisitId);

        if (diagnosesRepository.existsByMedicalVisitId(
                medicalVisitId)) {

            logger.warn(
                    "Eliminación rechazada: visita posee diagnósticos. medicalVisitId={}",
                    medicalVisitId);

            throw new RuntimeException(
                    "No se puede eliminar la visita porque tiene diagnósticos asociados");
        }

        if (vitalSignsRepository.existsByMedicalVisitId(
                medicalVisitId)) {

            logger.warn(
                    "Eliminación rechazada: visita posee signos vitales. medicalVisitId={}",
                    medicalVisitId);

            throw new RuntimeException(
                    "No se puede eliminar la visita porque tiene signos vitales asociados");
        }

        medicalVisitRepository.delete(medicalVisit);
    }

    public MedicalVisit findMedicalVisitEntityById(
            Long medicalVisitId) {

        return medicalVisitRepository.findById(medicalVisitId)
                .orElseThrow(() -> {
                    logger.warn(
                            "Visita médica no encontrada con ID {}",
                            medicalVisitId);

                    return new RuntimeException(
                            "Visita médica no encontrada");
                });
    }
}