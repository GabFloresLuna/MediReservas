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
import cl.duoc.users.dto.CreatePatientProfileRequestDTO;
import cl.duoc.users.dto.PatientProfileResponseDTO;
import cl.duoc.users.dto.UpdatePatientProfileRequestDTO;
import cl.duoc.users.service.PatientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patient-profiles")
@RequiredArgsConstructor
public class PatientProfileController {

	private final PatientProfileService patientProfileService;

	@PostMapping
	public ResponseEntity<ApiResponse<PatientProfileResponseDTO>> createPatientProfile(
			@Valid @RequestBody CreatePatientProfileRequestDTO request) {
		PatientProfileResponseDTO response = patientProfileService.createPatientProfile(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(
						201,
						"Perfil de paciente creado correctamente",
						response));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PatientProfileResponseDTO>>> getAllPatientProfiles() {
		List<PatientProfileResponseDTO> response = patientProfileService.getAllPatientProfiles();

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfiles de paciente obtenidos correctamente",
						response));
	}

	@GetMapping("/{patientProfileId}")
	public ResponseEntity<ApiResponse<PatientProfileResponseDTO>> getPatientProfileById(
			@PathVariable Long patientProfileId) {
		PatientProfileResponseDTO response = patientProfileService.getPatientProfileById(patientProfileId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil de paciente obtenido correctamente",
						response));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<PatientProfileResponseDTO>> getPatientProfileByUserId(
			@PathVariable Long userId) {
		PatientProfileResponseDTO response = patientProfileService.getPatientProfileByUserId(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil de paciente obtenido correctamente por usuario",
						response));
	}

	@GetMapping("/user/{userId}/exists")
	public ResponseEntity<ApiResponse<Boolean>> existsByUserId(
			@PathVariable Long userId) {
		boolean response = patientProfileService.existsByUserId(userId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Validación realizada correctamente",
						response));
	}

	@PutMapping("/{patientProfileId}")
	public ResponseEntity<ApiResponse<PatientProfileResponseDTO>> updatePatientProfile(
			@PathVariable Long patientProfileId,
			@Valid @RequestBody UpdatePatientProfileRequestDTO request) {
		PatientProfileResponseDTO response = patientProfileService.updatePatientProfile(
				patientProfileId,
				request);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil de paciente actualizado correctamente",
						response));
	}

	@DeleteMapping("/{patientProfileId}")
	public ResponseEntity<ApiResponse<Object>> deletePatientProfile(
			@PathVariable Long patientProfileId) {
		patientProfileService.deletePatientProfile(patientProfileId);

		return ResponseEntity.ok(
				new ApiResponse<>(
						200,
						"Perfil de paciente eliminado correctamente",
						null));
	}
}