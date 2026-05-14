package cl.duoc.users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.users.dto.ApiResponse;
import cl.duoc.users.dto.CreateUserRequestDTO;
import cl.duoc.users.dto.UserResponseDTO;
import cl.duoc.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
			@Valid @RequestBody CreateUserRequestDTO request) {
		UserResponseDTO response = userService.createUser(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(201, "Usuario creado correctamente", response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
		List<UserResponseDTO> response = userService.getAllUsers();

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Usuarios obtenidos correctamente", response));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
			@PathVariable Long userId) {
		UserResponseDTO response = userService.getUserById(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Usuario obtenido correctamente", response));
	}

	@GetMapping("/{userId}/exists")
	public ResponseEntity<ApiResponse<Boolean>> existsById(
			@PathVariable Long userId) {
		boolean exists = userService.existsById(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Validación realizada correctamente", exists));
	}

	@GetMapping("/run/{run}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByRun(
			@PathVariable String run) {
		UserResponseDTO response = userService.getUserByRun(run);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Usuario obtenido correctamente por RUN", response));
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(
			@PathVariable String email) {
		UserResponseDTO response = userService.getUserByEmail(email);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Usuario obtenido correctamente por correo", response));
	}
}