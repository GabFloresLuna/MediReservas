package cl.duoc.users.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreateUserProfileRequestDTO;
import cl.duoc.users.dto.UpdateUserProfileRequestDTO;
import cl.duoc.users.dto.UserProfileResponseDTO;
import cl.duoc.users.model.User;
import cl.duoc.users.model.UserProfile;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final PatientProfileRepository patientProfileRepository;
    private final ReceptionistProfileRepository receptionistProfileRepository;
    private final AdministratorProfileRepository administratorProfileRepository;

    public UserProfileResponseDTO createUserProfile(CreateUserProfileRequestDTO request) {

        if (userProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn("Creación de perfil general rechazada: el usuario ya tiene perfil general. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil general");
        }

        User user = userService.findUserEntityById(request.userId());

        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhone(request.phone());
        profile.setBirthDate(request.birthDate());
        profile.setAddress(request.address());

        UserProfile savedProfile = userProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public UserProfileResponseDTO getProfileByUserId(Long userId) {
        UserProfile profile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: perfil general no encontrado para userId={}", userId);
                    return new RuntimeException("Perfil general no encontrado");
                });

        return toResponseDTO(profile);
    }

    public List<UserProfileResponseDTO> getAllProfiles() {
        return userProfileRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UserProfileResponseDTO getProfileById(Long profileId) {
        UserProfile profile = userProfileRepository.findById(profileId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: perfil general no encontrado con ID {}", profileId);
                    return new RuntimeException("Perfil general no encontrado");
                });

        return toResponseDTO(profile);
    }

    public boolean existsByUserId(Long userId) {
        return userProfileRepository.existsByUserUserId(userId);
    }

    public UserProfileResponseDTO updateUserProfile(Long userProfileId, UpdateUserProfileRequestDTO request) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> {
                    logger.warn("Actualización rechazada: perfil general no encontrado con ID {}", userProfileId);
                    return new RuntimeException("Perfil general no encontrado");
                });

        profile.setFirstName(request.firstName());
        profile.setLastName(request.lastName());
        profile.setPhone(request.phone());
        profile.setBirthDate(request.birthDate());
        profile.setAddress(request.address());

        UserProfile savedProfile = userProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public void deleteUserProfile(Long userProfileId) {
        UserProfile profile = userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> {
                    logger.warn("Eliminación rechazada: perfil general no encontrado con ID {}", userProfileId);
                    return new RuntimeException("Perfil general no encontrado");
                });

        Long userId = profile.getUser().getUserId();

        if (patientProfileRepository.existsByUserUserId(userId)) {
            logger.warn("Eliminación rechazada: usuario tiene perfil de paciente. userId={}", userId);
            throw new RuntimeException(
                    "No se puede eliminar el perfil general porque el usuario tiene un perfil de paciente");
        }

        if (receptionistProfileRepository.existsByUserUserId(userId)) {
            logger.warn("Eliminación rechazada: usuario tiene perfil de recepcionista. userId={}", userId);
            throw new RuntimeException(
                    "No se puede eliminar el perfil general porque el usuario tiene un perfil de recepcionista");
        }

        if (administratorProfileRepository.existsByUserUserId(userId)) {
            logger.warn("Eliminación rechazada: usuario tiene perfil de administrador. userId={}", userId);
            throw new RuntimeException(
                    "No se puede eliminar el perfil general porque el usuario tiene un perfil de administrador");
        }

        userProfileRepository.delete(profile);
    }

    public UserProfileResponseDTO toResponseDTO(UserProfile profile) {
        return new UserProfileResponseDTO(
                profile.getUserProfileId(),
                profile.getUser().getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getBirthDate(),
                profile.getAddress());
    }
}