package cl.duoc.medical_records.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.service.VitalSignsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vital-signs")
public class VitalSignsController {

    private final VitalSignsService vitalSignsService;
    private static final Logger logger = LoggerFactory.getLogger(VitalSignsController.class);

    @PostMapping()
    @Operation(summary = "Registra signos vitales", description = "Permite registrar los signos vitales relacionados al ID de visita médica")
    public ResponseEntity<ApiResponse<VitalSignResponseDTO>> createVitalSigns(@Valid @RequestBody CreateVitalSignRequestDTO requestDTO) {
        try {
            VitalSignResponseDTO vitalSign = vitalSignsService.create(requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Signos vitales creados", vitalSign));
        } catch (Exception e) {
            logger.error("Error al registrar signos vitales: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al registrar signos vitales: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{vitalSignId}")
    @Operation(summary = "Obtiene signos vitales por ID", description = "Permite obtener signos vitales por su ID")
    public ResponseEntity<ApiResponse<VitalSignResponseDTO>> getVitalSignById(@PathVariable Long vitalSignId) {
        try {
            VitalSignResponseDTO vitalSign = vitalSignsService.findById(vitalSignId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Signos vitales encontrados", vitalSign));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        }
    }

    @GetMapping("/medical-visit/{medicalVisitId}")
    @Operation(summary = "Obtiene signos vitales por visita médica", description = "Permite obtener todos los signos vitales registrados en una visita médica")
    public ResponseEntity<ApiResponse<java.util.List<VitalSignResponseDTO>>> getVitalSignsByMedicalVisit(@PathVariable Long medicalVisitId) {
        try {
            java.util.List<VitalSignResponseDTO> vitalSigns = vitalSignsService.findByMedicalVisitId(medicalVisitId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Signos vitales encontrados", vitalSigns));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{vitalSignId}")
    @Operation(summary = "Actualiza signos vitales", description = "Permite actualizar los signos vitales existentes")
    public ResponseEntity<ApiResponse<VitalSignResponseDTO>> updateVitalSigns(
            @PathVariable Long vitalSignId,
            @Valid @RequestBody UpdateVitalSignRequestDTO requestDTO) {
        try {
            VitalSignResponseDTO updatedVitalSign = vitalSignsService.update(vitalSignId, requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Signos vitales actualizados", updatedVitalSign));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al actualizar: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{vitalSignId}")
    @Operation(summary = "Elimina signos vitales", description = "Permite eliminar un registro de signos vitales")
    public ResponseEntity<ApiResponse<Void>> deleteVitalSigns(@PathVariable Long vitalSignId) {
        try {
            vitalSignsService.delete(vitalSignId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Signos vitales eliminados correctamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al eliminar: " + e.getMessage(), null));
        }
    }
}