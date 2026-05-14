package cl.duoc.users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreateUserProfileRequestDTO;
import cl.duoc.users.dto.UserProfileResponseDTO;
import cl.duoc.users.model.User;
import cl.duoc.users.model.UserProfile;
import cl.duoc.users.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public UserProfileResponseDTO createUserProfile(CreateUserProfileRequestDTO request) {

        if (userProfileRepository.existsByUserUserId(request.userId())) {
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
                .orElseThrow(() -> new RuntimeException("Perfil general no encontrado"));

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
                .orElseThrow(() -> new RuntimeException("Perfil general no encontrado"));

        return toResponseDTO(profile);
    }

    public boolean existsByUserId(Long userId) {
        return userProfileRepository.existsByUserUserId(userId);
}

    public UserProfileResponseDTO toResponseDTO(UserProfile profile) {
        return new UserProfileResponseDTO(
                profile.getUserProfileId(),
                profile.getUser().getUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPhone(),
                profile.getBirthDate(),
                profile.getAddress()
        );
    }
}