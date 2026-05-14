package cl.duoc.users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreateReceptionistProfileRequestDTO;
import cl.duoc.users.dto.ReceptionistProfileResponseDTO;
import cl.duoc.users.model.ReceptionistProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceptionistProfileService {

    private final ReceptionistProfileRepository receptionistProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final AdministratorProfileRepository administratorProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public ReceptionistProfileResponseDTO createReceptionistProfile(CreateReceptionistProfileRequestDTO request) {

        if (!userProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException(
                    "El usuario debe tener un perfil general antes de crear el perfil de recepcionista");
        }

        if (receptionistProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de recepcionista");
        }

        if (patientProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de paciente");
        }

        if (administratorProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de administrador");
        }

        User user = userService.findUserEntityById(request.userId());

        ReceptionistProfile profile = new ReceptionistProfile();
        profile.setUser(user);
        profile.setShift(request.shift());
        profile.setDepartment(request.department());

        ReceptionistProfile savedProfile = receptionistProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public ReceptionistProfileResponseDTO getReceptionistProfileByUserId(Long userId) {
        ReceptionistProfile profile = receptionistProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de recepcionista no encontrado"));

        return toResponseDTO(profile);
    }

    public List<ReceptionistProfileResponseDTO> getAllReceptionistProfiles() {
        return receptionistProfileRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ReceptionistProfileResponseDTO getReceptionistProfileById(Long receptionistProfileId) {
        ReceptionistProfile profile = receptionistProfileRepository.findById(receptionistProfileId)
                .orElseThrow(() -> new RuntimeException("Perfil de recepcionista no encontrado"));

        return toResponseDTO(profile);
    }

    public boolean existsByUserId(Long userId) {
        return receptionistProfileRepository.existsByUserUserId(userId);
    }

    public ReceptionistProfileResponseDTO toResponseDTO(ReceptionistProfile profile) {
        return new ReceptionistProfileResponseDTO(
                profile.getReceptionistProfileId(),
                profile.getUser().getUserId(),
                profile.getShift(),
                profile.getDepartment());
    }
}