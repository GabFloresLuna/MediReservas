package cl.duoc.users.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.users.client.AuthClient;
import cl.duoc.users.dto.AdministratorProfileResponseDTO;
import cl.duoc.users.dto.CreateAdministratorProfileRequestDTO;
import cl.duoc.users.dto.UpdateAdministratorProfileRequestDTO;
import cl.duoc.users.model.AdministratorProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministratorProfileService {

    private static final Logger logger = LoggerFactory.getLogger(AdministratorProfileService.class);

    private final AdministratorProfileRepository administratorProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final ReceptionistProfileRepository receptionistProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final AuthClient authClient;

    public AdministratorProfileResponseDTO createAdministratorProfile(CreateAdministratorProfileRequestDTO request) {

        if (!userProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn("Creación de perfil de administrador rechazada: usuario sin perfil general. userId={}",
                    request.userId());
            throw new RuntimeException(
                    "El usuario debe tener un perfil general antes de crear un perfil de administrador");
        }

        if (administratorProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn(
                    "Creación de perfil de administrador rechazada: usuario ya tiene perfil de administrador. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil de administrador");
        }

        if (patientProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn("Creación de perfil de administrador rechazada: usuario ya tiene perfil de paciente. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil de paciente");
        }

        if (receptionistProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn(
                    "Creación de perfil de administrador rechazada: usuario ya tiene perfil de recepcionista. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil de recepcionista");
        }

        User user = userService.findUserEntityById(request.userId());

        AdministratorProfile profile = new AdministratorProfile();
        profile.setUser(user);
        profile.setDepartment(request.department());
        profile.setPositionName(request.positionName());

        AdministratorProfile savedProfile = administratorProfileRepository.save(profile);

        authClient.assignRole(user.getAuthUserId(), "ADMIN");

        return toResponseDTO(savedProfile);
    }

    public AdministratorProfileResponseDTO getAdministratorProfileByUserId(Long userId) {
        AdministratorProfile profile = administratorProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: perfil de administrador no encontrado para userId={}", userId);
                    return new RuntimeException("Perfil de administrador no encontrado");
                });

        return toResponseDTO(profile);
    }

    public List<AdministratorProfileResponseDTO> getAllAdministratorProfiles() {
        return administratorProfileRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public AdministratorProfileResponseDTO getAdministratorProfileById(Long administratorProfileId) {
        AdministratorProfile profile = administratorProfileRepository.findById(administratorProfileId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: perfil de administrador no encontrado con ID {}",
                            administratorProfileId);
                    return new RuntimeException("Perfil de administrador no encontrado");
                });

        return toResponseDTO(profile);
    }

    public boolean existsByUserId(Long userId) {
        return administratorProfileRepository.existsByUserUserId(userId);
    }

    public AdministratorProfileResponseDTO updateAdministratorProfile(
            Long administratorProfileId,
            UpdateAdministratorProfileRequestDTO request) {
        AdministratorProfile profile = administratorProfileRepository.findById(administratorProfileId)
                .orElseThrow(() -> {
                    logger.warn("Actualización rechazada: perfil de administrador no encontrado con ID {}",
                            administratorProfileId);
                    return new RuntimeException("Perfil de administrador no encontrado");
                });

        profile.setDepartment(request.department());
        profile.setPositionName(request.positionName());

        AdministratorProfile savedProfile = administratorProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public void deleteAdministratorProfile(Long administratorProfileId) {
        AdministratorProfile profile = administratorProfileRepository.findById(administratorProfileId)
                .orElseThrow(() -> {
                    logger.warn("Eliminación rechazada: perfil de administrador no encontrado con ID {}",
                            administratorProfileId);
                    return new RuntimeException("Perfil de administrador no encontrado");
                });

        administratorProfileRepository.delete(profile);
    }

    public AdministratorProfileResponseDTO toResponseDTO(AdministratorProfile profile) {
        return new AdministratorProfileResponseDTO(
                profile.getAdministratorProfileId(),
                profile.getUser().getUserId(),
                profile.getDepartment(),
                profile.getPositionName());
    }
}