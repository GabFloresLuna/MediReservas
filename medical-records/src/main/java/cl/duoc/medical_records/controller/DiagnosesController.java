package cl.duoc.medical_records.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.service.DiagnosesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diagnoses")
public class DiagnosesController {
    
    private final DiagnosesService diagnosesService;
    private static final Logger logger = LoggerFactory.getLogger(DiagnosesController.class);

    @PostMapping()
    @Operation(summary = "Crear diagnóstico", description = "Permite crear diagnóstico con id del paciente, descripción y notas")
    public ResponseEntity<ApiResponse<DiagnosisResponseDTO>> createDiagnosis(@Valid @RequestBody CreateDiagnosisRequestDTO requestDTO) {
        try {
            DiagnosisResponseDTO diagnosis = diagnosesService.create(requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Diagnóstico creado", diagnosis));
        } catch (Exception e) {
            logger.error("Error al registrar diagnóstico: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al registrar diagnóstico: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener diagnóstico por ID", description = "Permite obtener un diagnóstico por su ID")
    public ResponseEntity<ApiResponse<DiagnosisResponseDTO>> getDiagnosisById(@PathVariable Long id) {
        try {
            DiagnosisResponseDTO diagnosis = diagnosesService.findById(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Diagnóstico encontrado", diagnosis));
        } catch (Exception e) {
            logger.error("Error al obtener diagnóstico: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @GetMapping("/medical-visit/{medicalVisitId}")
    @Operation(summary = "Obtener diagnósticos por visita médica", description = "Permite obtener todos los diagnósticos de una visita médica")
    public ResponseEntity<ApiResponse<List<DiagnosisResponseDTO>>> getDiagnosesByMedicalVisit(@PathVariable Long medicalVisitId) {
        try {
            List<DiagnosisResponseDTO> diagnoses = diagnosesService.findByMedicalVisitId(medicalVisitId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Diagnósticos encontrados", diagnoses));
        } catch (Exception e) {
            logger.error("Error al obtener diagnósticos: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar diagnóstico", description = "Permite actualizar un diagnóstico existente")
    public ResponseEntity<ApiResponse<DiagnosisResponseDTO>> updateDiagnosis(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDiagnosisRequestDTO requestDTO) {
        try {
            DiagnosisResponseDTO updatedDiagnosis = diagnosesService.update(id, requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Diagnóstico actualizado", updatedDiagnosis));
        } catch (RuntimeException e) {
            logger.error("Error al actualizar diagnóstico: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error al actualizar diagnóstico: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al actualizar diagnóstico: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar diagnóstico", description = "Permite eliminar un diagnóstico existente")
    public ResponseEntity<ApiResponse<Void>> deleteDiagnosis(@PathVariable Long id) {
        try {
            diagnosesService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Diagnóstico eliminado correctamente", null));
        } catch (RuntimeException e) {
            logger.error("Error al eliminar diagnóstico: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error al eliminar diagnóstico: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al eliminar diagnóstico: " + e.getMessage(), null));
        }
    }
}