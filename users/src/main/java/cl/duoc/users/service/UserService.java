package cl.duoc.users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreateUserRequestDTO;
import cl.duoc.users.dto.UserResponseDTO;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByAuthUserId(request.authUserId())) {
            throw new RuntimeException("Ya existe un usuario con ese authUserId");
        }

        if (userRepository.existsByRun(request.run())) {
            throw new RuntimeException("Ya existe un usuario con ese RUN");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        User user = new User();
        user.setAuthUserId(request.authUserId());
        user.setRun(request.run());
        user.setEmail(request.email());
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UserResponseDTO getUserById(Long userId) {
        User user = findUserEntityById(userId);
        return toResponseDTO(user);
    }

    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    public User findUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getUserId(),
                user.getAuthUserId(),
                user.getRun(),
                user.getEmail(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}