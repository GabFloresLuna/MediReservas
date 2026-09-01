package cl.duoc.medical_records.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.medical_records.dto.CreateVitalSignRequestDTO;
import cl.duoc.medical_records.dto.UpdateVitalSignRequestDTO;
import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.model.VitalSigns;
import cl.duoc.medical_records.repository.VitalSignsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VitalSignsService {

    private static final Logger logger = LoggerFactory.getLogger(VitalSignsService.class);

    private final VitalSignsRepository vitalSignsRepository;
    private final MedicalVisitService medicalVisitService;
    private final ToDTO toDTO;

    public VitalSignResponseDTO create(
            CreateVitalSignRequestDTO requestDTO) {

        MedicalVisit medicalVisit = medicalVisitService.findMedicalVisitEntityById(
                requestDTO.medicalVisitId());

        VitalSigns vitalSigns = toDTO.toVitalSigns(requestDTO);

        vitalSigns.setMedicalVisit(medicalVisit);

        VitalSigns saved = vitalSignsRepository.save(vitalSigns);

        return toDTO.toVitalSignResponseDTO(saved);
    }

    public VitalSignResponseDTO findById(
            Long vitalSignId) {

        VitalSigns vitalSigns = findVitalSignsEntityById(vitalSignId);

        return toDTO.toVitalSignResponseDTO(vitalSigns);
    }

    public List<VitalSignResponseDTO> findByMedicalVisitId(
            Long medicalVisitId) {

        medicalVisitService.findMedicalVisitEntityById(
                medicalVisitId);

        return vitalSignsRepository
                .findByMedicalVisitId(medicalVisitId)
                .stream()
                .map(toDTO::toVitalSignResponseDTO)
                .toList();
    }

    public VitalSignResponseDTO update(
            Long vitalSignId,
            UpdateVitalSignRequestDTO requestDTO) {

        VitalSigns vitalSigns = findVitalSignsEntityById(vitalSignId);

        if (requestDTO.temperature() != null) {
            vitalSigns.setTemperature(
                    requestDTO.temperature());
        }

        if (requestDTO.bloodPressure() != null) {
            vitalSigns.setBloodPressure(
                    requestDTO.bloodPressure());
        }

        if (requestDTO.heartRate() != null) {
            vitalSigns.setHeartRate(
                    requestDTO.heartRate());
        }

        if (requestDTO.weight() != null) {
            vitalSigns.setWeight(
                    requestDTO.weight());
        }

        if (requestDTO.height() != null) {
            vitalSigns.setHeight(
                    requestDTO.height());
        }

        VitalSigns updated = vitalSignsRepository.save(vitalSigns);

        return toDTO.toVitalSignResponseDTO(updated);
    }

    public void delete(Long vitalSignId) {
        VitalSigns vitalSigns = findVitalSignsEntityById(vitalSignId);

        vitalSignsRepository.delete(vitalSigns);
    }

    public VitalSigns findVitalSignsEntityById(
            Long vitalSignId) {

        return vitalSignsRepository.findById(vitalSignId)
                .orElseThrow(() -> {
                    logger.warn(
                            "Signos vitales no encontrados con ID {}",
                            vitalSignId);

                    return new RuntimeException(
                            "Signos vitales no encontrados");
                });
    }
}