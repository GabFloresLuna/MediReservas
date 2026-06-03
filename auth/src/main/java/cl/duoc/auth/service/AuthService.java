package cl.duoc.auth.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.duoc.auth.dto.AuthResponseDTO;
import cl.duoc.auth.dto.AuthUserResponseDTO;
import cl.duoc.auth.dto.ChangePasswordRequestDTO;
import cl.duoc.auth.dto.LoginRequestDTO;
import cl.duoc.auth.dto.RegisterRequestDTO;
import cl.duoc.auth.dto.RoleResponseDTO;
import cl.duoc.auth.model.AuthUser;
import cl.duoc.auth.model.Role;
import cl.duoc.auth.repository.AuthUserRepository;
import cl.duoc.auth.repository.RoleRepository;
import cl.duoc.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (authUserRepository.existsByEmail(request.email())) {
            logger.warn("Registro rechazado: ya existe usuario con correo {}", request.email());
            throw new RuntimeException("Ya existe un usuario registrado con ese correo");
        }

        Role role = roleRepository.findByRoleName(request.roleName())
                .orElseThrow(() -> {
                    logger.warn("Registro rechazado: rol no encontrado {}", request.roleName());
                    return new RuntimeException("Rol no encontrado");
                });

        if (!role.isActive()) {
            logger.warn("Registro rechazado: rol inactivo {}", request.roleName());
            throw new RuntimeException("El rol seleccionado no está activo");
        }

        AuthUser authUser = new AuthUser();
        authUser.setEmail(request.email());
        authUser.setPasswordHash(passwordEncoder.encode(request.password()));
        authUser.setEnabled(true);
        authUser.setRoles(Set.of(role));

        AuthUser savedUser = authUserRepository.save(authUser);

        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponseDTO(
                savedUser.getAuthUserId(),
                savedUser.getEmail(),
                token,
                savedUser.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .toList());
    }

    public AuthResponseDTO login(LoginRequestDTO request) {

        AuthUser authUser = authUserRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    logger.warn("Login rechazado: correo no registrado {}", request.email());
                    return new RuntimeException("Credenciales inválidas");
                });

        if (!authUser.isEnabled()) {
            logger.warn("Login rechazado: usuario deshabilitado {}", request.email());
            throw new RuntimeException("El usuario está deshabilitado");
        }

        if (!passwordEncoder.matches(request.password(), authUser.getPasswordHash())) {
            logger.warn("Login rechazado: contraseña incorrecta para {}", request.email());
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(authUser.getEmail());

        return new AuthResponseDTO(
                authUser.getAuthUserId(),
                authUser.getEmail(),
                token,
                authUser.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .toList());
    }

    public List<AuthUserResponseDTO> getAllUsers() {
        return authUserRepository.findAll()
                .stream()
                .map(this::toAuthUserResponseDTO)
                .toList();
    }

    public AuthUserResponseDTO getUserById(Long authUserId) {
        AuthUser authUser = findAuthUserEntityById(authUserId);
        return toAuthUserResponseDTO(authUser);
    }

    public boolean existsById(Long authUserId) {
        return authUserRepository.existsById(authUserId);
    }

    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public String extractEmailFromToken(String token) {
        return jwtUtil.extractEmail(token);
    }

    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toRoleResponseDTO)
                .toList();
    }

    public AuthUser findAuthUserEntityById(Long authUserId) {
        return authUserRepository.findById(authUserId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: usuario de autenticación no encontrado con ID {}", authUserId);
                    return new RuntimeException("Usuario de autenticación no encontrado");
                });
    }

    public RoleResponseDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: rol no encontrado con ID {}", roleId);
                    return new RuntimeException("Rol no encontrado");
                });

        return toRoleResponseDTO(role);
    }

    public RoleResponseDTO getRoleByName(String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: rol no encontrado con nombre {}", roleName);
                    return new RuntimeException("Rol no encontrado");
                });

        return toRoleResponseDTO(role);
    }

    public boolean roleExistsById(Long roleId) {
        return roleRepository.existsById(roleId);
    }

    public boolean roleExistsActiveById(Long roleId) {
        return roleRepository.existsByRoleIdAndActiveTrue(roleId);
    }

    public RoleResponseDTO activateRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    logger.warn("Activación de rol rechazada: rol no encontrado con ID {}", roleId);
                    return new RuntimeException("Rol no encontrado");
                });

        if (role.isActive()) {
            logger.warn("Activación de rol rechazada: el rol ya estaba activo. roleId={}", roleId);
            throw new RuntimeException("El rol ya está activado");
        }

        role.setActive(true);

        Role savedRole = roleRepository.save(role);

        return toRoleResponseDTO(savedRole);
    }

    public RoleResponseDTO deactivateRole(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> {
                    logger.warn("Desactivación de rol rechazada: rol no encontrado con ID {}", roleId);
                    return new RuntimeException("Rol no encontrado");
                });

        if (!role.isActive()) {
            logger.warn("Desactivación de rol rechazada: el rol ya estaba desactivado. roleId={}", roleId);
            throw new RuntimeException("El rol ya está desactivado");
        }

        role.setActive(false);

        Role savedRole = roleRepository.save(role);

        return toRoleResponseDTO(savedRole);
    }

    public AuthUserResponseDTO getUserByEmail(String email) {
        AuthUser authUser = authUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Búsqueda rechazada: usuario de autenticación no encontrado con correo {}", email);
                    return new RuntimeException("Usuario de autenticación no encontrado");
                });

        return toAuthUserResponseDTO(authUser);
    }

    public AuthUserResponseDTO enableUser(Long authUserId) {
        AuthUser authUser = findAuthUserEntityById(authUserId);

        if (authUser.isEnabled()) {
            logger.warn("Habilitación rechazada: el usuario ya estaba habilitado. authUserId={}", authUserId);
            throw new RuntimeException("El usuario ya está habilitado");
        }

        authUser.setEnabled(true);

        AuthUser savedUser = authUserRepository.save(authUser);

        return toAuthUserResponseDTO(savedUser);
    }

    public AuthUserResponseDTO disableUser(Long authUserId) {
        AuthUser authUser = findAuthUserEntityById(authUserId);

        if (!authUser.isEnabled()) {
            logger.warn("Deshabilitación rechazada: el usuario ya estaba deshabilitado. authUserId={}", authUserId);
            throw new RuntimeException("El usuario ya está deshabilitado");
        }

        authUser.setEnabled(false);

        AuthUser savedUser = authUserRepository.save(authUser);

        return toAuthUserResponseDTO(savedUser);
    }

    public AuthUserResponseDTO changePassword(Long authUserId, ChangePasswordRequestDTO request) {
        AuthUser authUser = findAuthUserEntityById(authUserId);

        if (!passwordEncoder.matches(request.currentPassword(), authUser.getPasswordHash())) {
            logger.warn("Cambio de contraseña rechazado: contraseña actual incorrecta para authUserId={}", authUserId);
            throw new RuntimeException("Credenciales inválidas");
        }

        authUser.setPasswordHash(passwordEncoder.encode(request.newPassword()));

        AuthUser savedUser = authUserRepository.save(authUser);

        return toAuthUserResponseDTO(savedUser);
    }

    private AuthUserResponseDTO toAuthUserResponseDTO(AuthUser authUser) {
        return new AuthUserResponseDTO(
                authUser.getAuthUserId(),
                authUser.getEmail(),
                authUser.isEnabled(),
                authUser.getCreatedAt(),
                authUser.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .toList());
    }

    private RoleResponseDTO toRoleResponseDTO(Role role) {
        return new RoleResponseDTO(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription(),
                role.isActive());
    }
}