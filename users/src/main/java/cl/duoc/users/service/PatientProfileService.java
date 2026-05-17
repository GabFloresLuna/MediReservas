package cl.duoc.users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreatePatientProfileRequestDTO;
import cl.duoc.users.dto.PatientProfileResponseDTO;
import cl.duoc.users.dto.UpdatePatientProfileRequestDTO;
import cl.duoc.users.model.PatientProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final ReceptionistProfileRepository receptionistProfileRepository;
    private final AdministratorProfileRepository administratorProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public PatientProfileResponseDTO createPatientProfile(CreatePatientProfileRequestDTO request) {

        if (!userProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario debe tener un perfil general antes de crear un perfil de paciente");
        }

        if (patientProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de paciente");
        }

        if (receptionistProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de recepcionista");
        }

        if (administratorProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de administrador");
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

    public List<PatientProfileResponseDTO> getAllPatientProfiles() {
        return patientProfileRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PatientProfileResponseDTO getPatientProfileById(Long patientProfileId) {
        PatientProfile profile = patientProfileRepository.findById(patientProfileId)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));

        return toResponseDTO(profile);
    }

    public boolean existsByUserId(Long userId) {
        return patientProfileRepository.existsByUserUserId(userId);
    }

    public PatientProfileResponseDTO updatePatientProfile(
            Long patientProfileId,
            UpdatePatientProfileRequestDTO request) {
        PatientProfile profile = patientProfileRepository.findById(patientProfileId)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));

        profile.setHealthInsurance(request.healthInsurance());
        profile.setEmergencyContactName(request.emergencyContactName());
        profile.setEmergencyContactPhone(request.emergencyContactPhone());
        profile.setBloodType(request.bloodType());
        profile.setAllergies(request.allergies());
        profile.setWeight(request.weight());

        PatientProfile savedProfile = patientProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public void deletePatientProfile(Long patientProfileId) {
        PatientProfile profile = patientProfileRepository.findById(patientProfileId)
                .orElseThrow(() -> new RuntimeException("Perfil de paciente no encontrado"));

        patientProfileRepository.delete(profile);
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
                profile.getWeight());
    }
}