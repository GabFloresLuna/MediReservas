package cl.duoc.medical_records.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateMedicalRecordRequestDTO;
import cl.duoc.medical_records.dto.MedicalRecordDetailResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalRecord;
import cl.duoc.medical_records.repository.MedicalRecordRepository;
import cl.duoc.medical_records.repository.MedicalVisitRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalVisitRepository medicalVisitRepository;
    private final ToDTO toDTO;

    public MedicalRecordResponseDTO create(
            CreateMedicalRecordRequestDTO requestDTO) {

        if (medicalRecordRepository.existsByPatientId(
                requestDTO.patientId())) {

            logger.warn(
                    "Creación de ficha médica rechazada: el paciente ya tiene ficha. patientId={}",
                    requestDTO.patientId());

            throw new RuntimeException(
                    "El paciente ya tiene un registro médico");
        }

        MedicalRecord medicalRecord = toDTO.toMedicalRecord(requestDTO);

        medicalRecord.setActive(true);

        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);

        return toDTO.toMedicalRecordResponseDTO(saved);
    }

    public List<MedicalRecordDetailResponseDTO> listAll() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(toDTO::toMedicalRecordDetailResponseDTO)
                .toList();
    }

    public MedicalRecordDetailResponseDTO findByPatientId(
            Long patientId) {

        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientId(patientId)
                .orElseThrow(() -> {
                    logger.warn(
                            "Registro médico no encontrado para patientId={}",
                            patientId);

                    return new RuntimeException(
                            "Registro médico no encontrado para el paciente");
                });

        return toDTO.toMedicalRecordDetailResponseDTO(
                medicalRecord);
    }

    public MedicalRecordDetailResponseDTO findByMedicalRecordId(
            Long medicalRecordId) {

        MedicalRecord medicalRecord = findMedicalRecordEntityById(medicalRecordId);

        return toDTO.toMedicalRecordDetailResponseDTO(
                medicalRecord);
    }

    public MedicalRecordResponseDTO activate(
            Long medicalRecordId) {

        MedicalRecord medicalRecord = findMedicalRecordEntityById(medicalRecordId);

        if (Boolean.TRUE.equals(medicalRecord.getActive())) {
            throw new RuntimeException(
                    "El registro médico ya se encuentra activo");
        }

        medicalRecord.setActive(true);

        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);

        return toDTO.toMedicalRecordResponseDTO(saved);
    }

    public MedicalRecordResponseDTO deactivate(
            Long medicalRecordId) {

        MedicalRecord medicalRecord = findMedicalRecordEntityById(medicalRecordId);

        if (Boolean.FALSE.equals(medicalRecord.getActive())) {
            throw new RuntimeException(
                    "El registro médico ya se encuentra inactivo");
        }

        medicalRecord.setActive(false);

        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);

        return toDTO.toMedicalRecordResponseDTO(saved);
    }

    public void delete(Long medicalRecordId) {
        MedicalRecord medicalRecord = findMedicalRecordEntityById(medicalRecordId);

        if (medicalVisitRepository.existsByMedicalRecordId(
                medicalRecordId)) {

            logger.warn(
                    "Eliminación rechazada: registro médico posee visitas. medicalRecordId={}",
                    medicalRecordId);

            throw new RuntimeException(
                    "No se puede eliminar el registro médico porque tiene visitas asociadas");
        }

        medicalRecordRepository.delete(medicalRecord);
    }

    public MedicalRecord findMedicalRecordEntityById(
            Long medicalRecordId) {

        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> {
                    logger.warn(
                            "Registro médico no encontrado con ID {}",
                            medicalRecordId);

                    return new RuntimeException(
                            "Registro médico no encontrado");
                });
    }
}