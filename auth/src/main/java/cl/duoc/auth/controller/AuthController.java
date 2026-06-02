package cl.duoc.auth.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.auth.dto.ApiResponse;
import cl.duoc.auth.dto.AuthResponseDTO;
import cl.duoc.auth.dto.AuthUserResponseDTO;
import cl.duoc.auth.dto.ChangePasswordRequestDTO;
import cl.duoc.auth.dto.LoginRequestDTO;
import cl.duoc.auth.dto.RegisterRequestDTO;
import cl.duoc.auth.dto.RoleResponseDTO;
import cl.duoc.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Permite registrar un nuevo usuario con email y contraseña.")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Usuario registrado correctamente", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite al usuario iniciar sesión mediante email y contraseña.")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Inicio de sesión correcto", response));
    }

    @GetMapping("/users")
    @Operation(summary = "Listar usuarios autenticados", description = "Lista todos los usuarios que se hayan registrado.")
    public ResponseEntity<ApiResponse<List<AuthUserResponseDTO>>> getAllUsers() {
        List<AuthUserResponseDTO> response = authService.getAllUsers();

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Usuarios de autenticación obtenidos correctamente", response));
    }

    @GetMapping("/users/{authUserId}")
    @Operation(summary = "Obtener usuario por id", description = "Obtiene la información de un usuario buscandolo mediante su id.")
    public ResponseEntity<ApiResponse<AuthUserResponseDTO>> getUserById(
            @PathVariable Long authUserId) {
        AuthUserResponseDTO response = authService.getUserById(authUserId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Usuario de autenticación obtenido correctamente", response));
    }

    @GetMapping("/users/{authUserId}/exists")
    @Operation(summary = "Valida si un usuario existe por id", description = "Busca un usuario por id y valida si este existe.")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @PathVariable Long authUserId) {
        boolean response = authService.existsById(authUserId);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Validación realizada correctamente", response));
    }

    @GetMapping("/roles")
    @Operation(summary = "Obtener roles", description = "Obtiene todos los roles que existen.")
    public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAllRoles() {
        List<RoleResponseDTO> response = authService.getAllRoles();

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Roles obtenidos correctamente", response));
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token", description = "Realiza la validación del token generado al registar/iniciar sesión.")
    public ResponseEntity<ApiResponse<Boolean>> validateToken(
            @RequestParam String token) {
        boolean response = authService.validateToken(token);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Validación de token realizada correctamente", response));
    }

    @GetMapping("/token/email")
    @Operation(summary = "Extraer email", description = "Extrae el email del usuario a partir del token")
    public ResponseEntity<ApiResponse<String>> extractEmailFromToken(
            @RequestParam String token) {
        String response = authService.extractEmailFromToken(token);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Correo extraído correctamente desde el token", response));
    }

    @GetMapping("/roles/{roleId}")
    @Operation(summary = "Obtener rol por id", description = "Obtiene la información de un rol a partir de su id.")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getRoleById(
            @PathVariable Long roleId) {
        RoleResponseDTO response = authService.getRoleById(roleId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Rol obtenido correctamente",
                        response));
    }

    @GetMapping("/roles/name/{roleName}")
    @Operation(summary = "Obtener rol por nombre", description = "Obtiene la información de un rol a partir del nombre.")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getRoleByName(
            @PathVariable String roleName) {
        RoleResponseDTO response = authService.getRoleByName(roleName);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Rol obtenido correctamente por nombre",
                        response));
    }

    @GetMapping("/roles/{roleId}/exists")
    @Operation(summary = "Valida si rol existe por id", description = "Busca un rol por id y valida si este existe.")
    public ResponseEntity<ApiResponse<Boolean>> roleExistsById(
            @PathVariable Long roleId) {
        boolean response = authService.roleExistsById(roleId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Validación de rol realizada correctamente",
                        response));
    }

    @GetMapping("/roles/{roleId}/active")
    @Operation(summary = "Valida si rol esta activo", description = "Busca un rol por id y valida si este se encuentra activo.")
    public ResponseEntity<ApiResponse<Boolean>> roleExistsActiveById(
            @PathVariable Long roleId) {
        boolean response = authService.roleExistsActiveById(roleId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Validación de rol activo realizada correctamente",
                        response));
    }

    @PatchMapping("/roles/{roleId}/activate")
    @Operation(summary = "Activa un rol", description = "Actualiza el estado de 'active' a verdadero.")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> activateRole(
            @PathVariable Long roleId) {
        RoleResponseDTO response = authService.activateRole(roleId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Rol activado correctamente",
                        response));
    }

    @PatchMapping("/roles/{roleId}/deactivate")
    @Operation(summary = "Desactiva un rol", description = "Actualiza el estado de 'active' a falso.")
    public ResponseEntity<ApiResponse<RoleResponseDTO>> deactivateRole(
            @PathVariable Long roleId) {
        RoleResponseDTO response = authService.deactivateRole(roleId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Rol desactivado correctamente",
                        response));
    }

    @GetMapping("/users/email/{email}")
    @Operation(summary = "Obtener usuario por email", description = "Obtiene la información de un usuario mediante su email.")
    public ResponseEntity<ApiResponse<AuthUserResponseDTO>> getUserByEmail(
            @PathVariable String email) {
        AuthUserResponseDTO response = authService.getUserByEmail(email);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Usuario de autenticación obtenido correctamente por correo",
                        response));
    }

    @PatchMapping("/users/{authUserId}/enable")
    @Operation(summary = "Activa un usuario", description = "Actualiza el estado de 'enabled' a verdadero.")
    public ResponseEntity<ApiResponse<AuthUserResponseDTO>> enableUser(
            @PathVariable Long authUserId) {
        AuthUserResponseDTO response = authService.enableUser(authUserId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Usuario habilitado correctamente",
                        response));
    }

    @PatchMapping("/users/{authUserId}/disable")
    @Operation(summary = "Desactiva un usuario", description = "Actualiza el estado de 'enabled' a falso.")
    public ResponseEntity<ApiResponse<AuthUserResponseDTO>> disableUser(
            @PathVariable Long authUserId) {
        AuthUserResponseDTO response = authService.disableUser(authUserId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Usuario deshabilitado correctamente",
                        response));
    }

    @PutMapping("/users/{authUserId}/password")
    @Operation(summary = "Cambiar contraseña", description = "Permite cambiar la contraseña de un usuario a partir de su id.")
    public ResponseEntity<ApiResponse<AuthUserResponseDTO>> changePassword(
            @PathVariable Long authUserId,
            @Valid @RequestBody ChangePasswordRequestDTO request) {
        AuthUserResponseDTO response = authService.changePassword(authUserId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        200,
                        "Contraseña actualizada correctamente",
                        response));
    }
}