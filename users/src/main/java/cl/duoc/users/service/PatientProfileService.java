package cl.duoc.users.service;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreatePatientProfileRequestDTO;
import cl.duoc.users.dto.PatientProfileResponseDTO;
import cl.duoc.users.model.PatientProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final UserService userService;

    public PatientProfileResponseDTO createPatientProfile(CreatePatientProfileRequestDTO request) {

        if (patientProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de paciente");
        }

        User user = userService.findUserEntityById(request.userId());

        PatientProfile profile = new PatientProfile();
        profile.setUser(user);
        profile.setHealthInsurance(request.healthInsurance());
        profile.setEmergencyContactName(request.emergencyContactName());
        profile.setEmergencyContactPhone(request.emergencyContactPhone());
        profile.setBloodType(request.bloodType());
        profile.setAllergies(request.allergies());
        profile.setWeight(request.weight());

        PatientProfile savedProfile = patientProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public PatientProfileResponseDTO getPatientProfileByUserId(Long userId) {
        PatientProfile profile = patientProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));

        return toResponseDTO(profile);
    }

    public PatientProfileResponseDTO toResponseDTO(PatientProfile profile) {
        return new PatientProfileResponseDTO(
                profile.getPatientProfileId(),
                profile.getUser().getUserId(),
                profile.getHealthInsurance(),
                profile.getEmergencyContactName(),
                profile.getEmergencyContactPhone(),
                profile.getBloodType(),
                profile.getAllergies(),
                profile.getWeight()
        );
    }
}