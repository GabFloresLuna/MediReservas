package cl.duoc.medical_records.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.duoc.medical_records.dto.*;
import cl.duoc.medical_records.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController {
    
    private final MedicalRecordService medicalRecordService;
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

    @GetMapping()
    @Operation(summary = "Lista los registros médicos", description = "Permite listar TODOS los registros médicos almacenados")
    public ResponseEntity<ApiResponse<List<MedicalRecordDetailResponseDTO>>> getAllMedicalRecords() {
        try {
            List<MedicalRecordDetailResponseDTO> medicalRecords = medicalRecordService.listAll();
            return ResponseEntity.ok(new ApiResponse<>(200, "Listado de registros médicos", medicalRecords));
        } catch (Exception e) {
            logger.error("Error al listar registros médicos: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al listar registros médicos: " + e.getMessage(), null));
        }
    }

    @PostMapping()
    @Operation(summary = "Crea un nuevo registro médico", description = "Permite crear un nuevo registro médico utilizando el ID del paciente")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequestDTO requestDTO) {
        try {
            MedicalRecordResponseDTO medicalRecord = medicalRecordService.create(requestDTO);
            return ResponseEntity.ok(new ApiResponse<>(200, "Registro médico creado", medicalRecord));
        } catch (Exception e) {
            logger.error("Error al crear registro médico: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al crear registro médico: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un registro médico por ID del paciente", description = "Permite obtener un registro médico consultando con el ID del paciente")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponseDTO>> getByPatientId(@PathVariable Long id) {
        try {
            MedicalRecordDetailResponseDTO medicalRecord = medicalRecordService.findByPatientId(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Registro médico encontrado", medicalRecord));
        } catch (RuntimeException e) {
            logger.error("Error al obtener registro médico: {}", e.getMessage());
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error al obtener registro médico: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al obtener registro médico: " + e.getMessage(), null));
        }
    }

    @GetMapping("/detail/{medicalRecordId}")
    @Operation(summary = "Obtiene detalle completo por ID de registro médico", description = "Permite obtener el detalle completo de un registro médico")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponseDTO>> getByMedicalRecordId(@PathVariable Long medicalRecordId) {
        try {
            MedicalRecordDetailResponseDTO medicalRecord = medicalRecordService.findByMedicalRecordId(medicalRecordId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Registro médico encontrado", medicalRecord));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{medicalRecordId}/deactivate")
    @Operation(summary = "Desactivar registro médico", description = "Permite desactivar un registro médico (soft delete)")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> deactivateMedicalRecord(@PathVariable Long medicalRecordId) {
        try {
            MedicalRecordResponseDTO updatedRecord = medicalRecordService.deactivate(medicalRecordId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Registro médico desactivado", updatedRecord));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{medicalRecordId}/activate")
    @Operation(summary = "Activar registro médico", description = "Permite activar un registro médico")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> activateMedicalRecord(@PathVariable Long medicalRecordId) {
        try {
            MedicalRecordResponseDTO updatedRecord = medicalRecordService.activate(medicalRecordId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Registro médico activado", updatedRecord));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{medicalRecordId}")
    @Operation(summary = "Eliminar registro médico", description = "Permite eliminar un registro médico (hard delete - solo si no tiene visitas)")
    public ResponseEntity<ApiResponse<Void>> deleteMedicalRecord(@PathVariable Long medicalRecordId) {
        try {
            medicalRecordService.delete(medicalRecordId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Registro médico eliminado correctamente", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Error al eliminar: " + e.getMessage(), null));
        }
    }
}