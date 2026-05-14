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

import cl.duoc.users.dto.AdministratorProfileResponseDTO;
import cl.duoc.users.dto.ApiResponse;
import cl.duoc.users.dto.CreateAdministratorProfileRequestDTO;
import cl.duoc.users.service.AdministratorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/administrator-profiles")
@RequiredArgsConstructor
public class AdministratorProfileController {

	private final AdministratorProfileService administratorProfileService;

	@PostMapping
	public ResponseEntity<ApiResponse<AdministratorProfileResponseDTO>> createAdministratorProfile(
			@Valid @RequestBody CreateAdministratorProfileRequestDTO request) {
		AdministratorProfileResponseDTO response = administratorProfileService.createAdministratorProfile(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(
						201,
						"Perfil de administrador creado correctamente",
						response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AdministratorProfileResponseDTO>>> getAllAdministratorProfiles() {
		List<AdministratorProfileResponseDTO> response = administratorProfileService.getAllAdministratorProfiles();

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfiles de administrador obtenidos correctamente",
						response));
	}

	@GetMapping("/{administratorProfileId}")
	public ResponseEntity<ApiResponse<AdministratorProfileResponseDTO>> getAdministratorProfileById(
			@PathVariable Long administratorProfileId) {
		AdministratorProfileResponseDTO response = administratorProfileService
				.getAdministratorProfileById(administratorProfileId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil de administrador obtenido correctamente",
						response));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<AdministratorProfileResponseDTO>> getAdministratorProfileByUserId(
			@PathVariable Long userId) {
		AdministratorProfileResponseDTO response = administratorProfileService
				.getAdministratorProfileByUserId(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil de administrador obtenido correctamente por usuario",
						response));
	}

	@GetMapping("/user/{userId}/exists")
	public ResponseEntity<ApiResponse<Boolean>> existsByUserId(
			@PathVariable Long userId) {
		boolean response = administratorProfileService.existsByUserId(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Validación realizada correctamente",
						response));
	}
}