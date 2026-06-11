package cl.duoc.users.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.users.client.AuthClient;
import cl.duoc.users.dto.CreateReceptionistProfileRequestDTO;
import cl.duoc.users.dto.ReceptionistProfileResponseDTO;
import cl.duoc.users.dto.UpdateReceptionistProfileRequestDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(ReceptionistProfileService.class);

    private final ReceptionistProfileRepository receptionistProfileRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final AdministratorProfileRepository administratorProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final AuthClient authClient;

    public ReceptionistProfileResponseDTO createReceptionistProfile(CreateReceptionistProfileRequestDTO request) {

        if (!userProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn("Creación de perfil de recepcionista rechazada: usuario sin perfil general. userId={}",
                    request.userId());
            throw new RuntimeException(
                    "El usuario debe tener un perfil general antes de crear el perfil de recepcionista");
        }

        if (receptionistProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn(
                    "Creación de perfil de recepcionista rechazada: usuario ya tiene perfil de recepcionista. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil de recepcionista");
        }

        if (patientProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn("Creación de perfil de recepcionista rechazada: usuario ya tiene perfil de paciente. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil de paciente");
        }

        if (administratorProfileRepository.existsByUserUserId(request.userId())) {
            logger.warn(
                    "Creación de perfil de recepcionista rechazada: usuario ya tiene perfil de administrador. userId={}",
                    request.userId());
            throw new RuntimeException("El usuario ya tiene un perfil de administrador");
        }

        User user = userService.findUserEntityById(request.userId());

        ReceptionistProfile profile = new ReceptionistProfile();
        profile.setUser(user);
        profile.setShift(request.shift());
        profile.setDepartment(request.department());

        ReceptionistProfile savedProfile = receptionistProfileRepository.save(profile);

        authClient.assignRole(user.getAuthUserId(), "RECEPTIONIST");

        return toResponseDTO(savedProfile);
    }

    public ReceptionistProfileResponseDTO getReceptionistProfileByUserId(Long userId) {
        ReceptionistProfile profile = receptionistProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: perfil de recepcionista no encontrado para userId={}", userId);
                    return new RuntimeException("Perfil de recepcionista no encontrado");
                });

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
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: perfil de recepcionista no encontrado con ID {}",
                            receptionistProfileId);
                    return new RuntimeException("Perfil de recepcionista no encontrado");
                });

        return toResponseDTO(profile);
    }

    public boolean existsByUserId(Long userId) {
        return receptionistProfileRepository.existsByUserUserId(userId);
    }

    public ReceptionistProfileResponseDTO updateReceptionistProfile(
            Long receptionistProfileId,
            UpdateReceptionistProfileRequestDTO request) {
        ReceptionistProfile profile = receptionistProfileRepository.findById(receptionistProfileId)
                .orElseThrow(() -> {
                    logger.warn("Actualización rechazada: perfil de recepcionista no encontrado con ID {}",
                            receptionistProfileId);
                    return new RuntimeException("Perfil de recepcionista no encontrado");
                });

        profile.setShift(request.shift());
        profile.setDepartment(request.department());

        ReceptionistProfile savedProfile = receptionistProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public void deleteReceptionistProfile(Long receptionistProfileId) {
        ReceptionistProfile profile = receptionistProfileRepository.findById(receptionistProfileId)
                .orElseThrow(() -> {
                    logger.warn("Eliminación rechazada: perfil de recepcionista no encontrado con ID {}",
                            receptionistProfileId);
                    return new RuntimeException("Perfil de recepcionista no encontrado");
                });

        receptionistProfileRepository.delete(profile);
    }

    public ReceptionistProfileResponseDTO toResponseDTO(ReceptionistProfile profile) {
        return new ReceptionistProfileResponseDTO(
                profile.getReceptionistProfileId(),
                profile.getUser().getUserId(),
                profile.getShift(),
                profile.getDepartment());
    }
}