package cl.duoc.medical_records.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.service.MedicalVisitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-visit")
public class MedicalVisitController {

    private final MedicalVisitService medicalVisitService;
    private static final Logger logger = LoggerFactory.getLogger(MedicalVisitController.class);

    @PostMapping()
    @Operation(summary = "Registra una visita médica", description = "Permite registrar una visita médica")
    public ResponseEntity<ApiResponse<MedicalVisitResponseDTO>> createMedicalVisit(@Valid @RequestBody CreateMedicalVisitRequestDTO requestDTO) {
        try {
            MedicalVisitResponseDTO medicalVisit = medicalVisitService.create(requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Visita médica creada", medicalVisit));
        } catch (Exception e) {
            logger.error("Error al crear una visita médica: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al crear una visita médica: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene visitas médicas por ID del paciente", description = "Permite obtener TODAS las visitas medicas asociadas al ID del paciente")
    public ResponseEntity<ApiResponse<List<MedicalVisitDetailReponseDTO>>> getAllByPatientId(@PathVariable Long id) {
        try {
            List<MedicalVisitDetailReponseDTO> medicalVisits = medicalVisitService.findAllByPatientId(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Visita(s) Médica(s) encontrada(s)", medicalVisits));
        } catch (Exception e) {
            logger.error("Error al encontrar visitas médicas: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al encontrar visitas médicas: " + e.getMessage(), null));
        }
    }

    @GetMapping("/detail/{medicalVisitId}")
    @Operation(summary = "Obtiene detalle de una visita médica por ID", description = "Permite obtener el detalle completo de una visita médica")
    public ResponseEntity<ApiResponse<MedicalVisitDetailReponseDTO>> getMedicalVisitDetail(@PathVariable Long medicalVisitId) {
        try {
            MedicalVisitDetailReponseDTO medicalVisit = medicalVisitService.findDetailById(medicalVisitId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Visita médica encontrada", medicalVisit));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @GetMapping("/record/{medicalRecordId}")
    @Operation(summary = "Obtiene visitas por registro médico", description = "Permite obtener todas las visitas de un registro médico")
    public ResponseEntity<ApiResponse<List<MedicalVisitResponseDTO>>> getByMedicalRecordId(@PathVariable Long medicalRecordId) {
        try {
            List<MedicalVisitResponseDTO> visits = medicalVisitService.findByMedicalRecordId(medicalRecordId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Visitas encontradas", visits));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{medicalVisitId}")
    @Operation(summary = "Actualiza una visita médica", description = "Permite actualizar los datos de una visita médica existente")
    public ResponseEntity<ApiResponse<MedicalVisitResponseDTO>> updateMedicalVisit(
            @PathVariable Long medicalVisitId,
            @Valid @RequestBody UpdateMedicalVisitRequestDTO requestDTO) {
        try {
            MedicalVisitResponseDTO updatedVisit = medicalVisitService.update(medicalVisitId, requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Visita médica actualizada", updatedVisit));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al actualizar: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{medicalVisitId}")
    @Operation(summary = "Elimina una visita médica", description = "Permite eliminar una visita médica (solo si no tiene diagnósticos ni signos vitales asociados)")
    public ResponseEntity<ApiResponse<Void>> deleteMedicalVisit(@PathVariable Long medicalVisitId) {
        try {
            medicalVisitService.delete(medicalVisitId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Visita médica eliminada correctamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al eliminar: " + e.getMessage(), null));
        }
    }
}