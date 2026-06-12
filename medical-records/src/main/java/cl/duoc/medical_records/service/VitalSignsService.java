package cl.duoc.medical_records.service;
 
import org.springframework.stereotype.Service;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.VitalSigns;
import cl.duoc.medical_records.repository.VitalSignsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VitalSignsService 
{
    private final ToDTO toDTO;

    private final VitalSignsRepository vitalSignsRepository;

    public VitalSignResponseDTO create(CreateVitalSignRequestDTO requestDTO) {
        VitalSigns vitalSigns = toDTO.toVitalSigns(requestDTO);
        VitalSigns saved = vitalSignsRepository.save(vitalSigns);
        return toDTO.toVitalSignResponseDTO(saved);
    }

    public VitalSignResponseDTO findById(Long vitalSignId) {
        VitalSigns vitalSigns = vitalSignsRepository.findById(vitalSignId)
            .orElseThrow(() -> new RuntimeException("Signos vitales no encontrados con ID: " + vitalSignId));
        return toDTO.toVitalSignResponseDTO(vitalSigns);
    }

    public List<VitalSignResponseDTO> findByMedicalVisitId(Long medicalVisitId) {
        return vitalSignsRepository.findByMedicalVisitId(medicalVisitId)
            .stream()
            .map(toDTO::toVitalSignResponseDTO)
            .collect(Collectors.toList());
    }

    public VitalSignResponseDTO update(Long vitalSignId, UpdateVitalSignRequestDTO requestDTO) {
        VitalSigns existingVitalSigns = vitalSignsRepository.findById(vitalSignId)
            .orElseThrow(() -> new RuntimeException("Signos vitales no encontrados con ID: " + vitalSignId));
        
        if (requestDTO.temperature() != null) {
            existingVitalSigns.setTemperature(requestDTO.temperature());
        }
        if (requestDTO.bloodPressure() != null) {
            existingVitalSigns.setBloodPressure(requestDTO.bloodPressure());
        }
        if (requestDTO.heartRate() != null) {
            existingVitalSigns.setHeartRate(requestDTO.heartRate());
        }
        if (requestDTO.weight() != null) {
            existingVitalSigns.setWeight(requestDTO.weight());
        }
        if (requestDTO.height() != null) {
            existingVitalSigns.setHeight(requestDTO.height());
        }
        
        VitalSigns updated = vitalSignsRepository.save(existingVitalSigns);
        return toDTO.toVitalSignResponseDTO(updated);
    }

    public void delete(Long vitalSignId) {
        if (!vitalSignsRepository.existsById(vitalSignId)) {
            throw new RuntimeException("Signos vitales no encontrados con ID: " + vitalSignId);
        }
        vitalSignsRepository.deleteById(vitalSignId);
    }
}