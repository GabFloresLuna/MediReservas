package cl.duoc.medical_records.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.medical_records.dto.ApiResponse; 
import cl.duoc.medical_records.dto.CreateMedicalRecordRequestDTO; 
import cl.duoc.medical_records.dto.MedicalRecordDetailResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordResponseDTO; 
import cl.duoc.medical_records.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController 
{
    private final MedicalRecordService medicalRecordService;
    private static final Logger logger = LoggerFactory.getLogger(VitalSignsController.class);

    @GetMapping()
    @Operation(summary = "Obtiene todos los registros medicos", description = "Permite obtener una lista de todos los registros medicos")
    public ResponseEntity<ApiResponse<List<MedicalRecordDetailResponseDTO>>> getAllMedicalRecords()
    {
        try
        {
            List<MedicalRecordDetailResponseDTO> medicalRecords = medicalRecordService.listAll();
            ApiResponse<List<MedicalRecordDetailResponseDTO>> response = 
                new ApiResponse<>
                (200,
                "Listado de registros médicos",
                medicalRecords
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error al registrar diagnostico: ", e.getMessage());
            ApiResponse<List<MedicalRecordDetailResponseDTO>> response =
                new ApiResponse<List<MedicalRecordDetailResponseDTO>>
                (
                    400,
                    "Error al registrar diagnostico: " + e.getMessage(),
                    null
                );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping()
    @Operation(summary = "Crea un registro medico", description = "Permite crear un registro medico")
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequestDTO requestDTO )
    {
        try
        {
            MedicalRecordResponseDTO medicalRecord = medicalRecordService.create(requestDTO);
            ApiResponse<MedicalRecordResponseDTO> response =
                new ApiResponse<>
                (
                    200,
                    "Registro médico creado",
                    medicalRecord
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error al registrar diagnostico: ", e.getMessage());
            ApiResponse<MedicalRecordResponseDTO> response =
                new ApiResponse<MedicalRecordResponseDTO>
                (
                    400,
                    "Error al registrar diagnostico: " + e.getMessage(),
                    null
                );
            return ResponseEntity.badRequest().body(response);
        }

    }
    
    @GetMapping("{id}")
    @Operation(summary = "Obtiene un registro medico por ID", description = "Permite obtener un registro medico a través del ID del paciente")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponseDTO>> getById(@PathVariable Long id)
    {
        try
        {
            MedicalRecordDetailResponseDTO medicalRecord = medicalRecordService.findByPatientId(id);
            ApiResponse<MedicalRecordDetailResponseDTO> response = 
                new ApiResponse<>
                (
                    200,
                    "Registro médico encontrado",
                    medicalRecord
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error al registrar diagnostico: ", e.getMessage());
            ApiResponse<MedicalRecordDetailResponseDTO> response =
                new ApiResponse<MedicalRecordDetailResponseDTO>
                (
                    400,
                    "Error al registrar diagnostico: " + e.getMessage(),
                    null
                );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
