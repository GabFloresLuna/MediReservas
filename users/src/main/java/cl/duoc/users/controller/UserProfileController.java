package cl.duoc.users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.users.dto.ApiResponse;
import cl.duoc.users.dto.CreateUserProfileRequestDTO;
import cl.duoc.users.dto.UpdateUserProfileRequestDTO;
import cl.duoc.users.dto.UserProfileResponseDTO;
import cl.duoc.users.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

	private final UserProfileService userProfileService;

	@PostMapping
	public ResponseEntity<ApiResponse<UserProfileResponseDTO>> createUserProfile(
			@Valid @RequestBody CreateUserProfileRequestDTO request) {
		UserProfileResponseDTO response = userProfileService.createUserProfile(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(201, "Perfil general creado correctamente", response));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getProfileByUserId(
			@PathVariable Long userId) {
		UserProfileResponseDTO response = userProfileService.getProfileByUserId(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Perfil general obtenido correctamente", response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<UserProfileResponseDTO>>> getAllProfiles() {
		List<UserProfileResponseDTO> response = userProfileService.getAllProfiles();

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Perfiles generales obtenidos correctamente", response));
	}

	@GetMapping("/{profileId}")
	public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getProfileById(
			@PathVariable Long profileId) {
		UserProfileResponseDTO response = userProfileService.getProfileById(profileId);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Perfil general obtenido correctamente", response));
	}

	@GetMapping("/user/{userId}/exists")
	public ResponseEntity<ApiResponse<Boolean>> existsByUserId(
			@PathVariable Long userId) {
		boolean response = userProfileService.existsByUserId(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(200, "Validación realizada correctamente", response));
	}

	@PutMapping("/{userProfileId}")
	public ResponseEntity<ApiResponse<UserProfileResponseDTO>> updateUserProfile(
			@PathVariable Long userProfileId,
			@Valid @RequestBody UpdateUserProfileRequestDTO request) {
		UserProfileResponseDTO response = userProfileService.updateUserProfile(userProfileId, request);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil general actualizado correctamente",
						response));
	}

	@DeleteMapping("/{userProfileId}")
	public ResponseEntity<ApiResponse<Object>> deleteUserProfile(
			@PathVariable Long userProfileId) {
		userProfileService.deleteUserProfile(userProfileId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil general eliminado correctamente",
						null));
	}
}