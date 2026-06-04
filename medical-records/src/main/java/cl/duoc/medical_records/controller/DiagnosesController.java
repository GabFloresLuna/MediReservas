package cl.duoc.medical_records.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.medical_records.dto.ApiResponse;
import cl.duoc.medical_records.dto.CreateDiagnosisRequestDTO;
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.service.DiagnosesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diagnoses")
public class DiagnosesController 
{ 
    private final DiagnosesService diagnosesService;
    private static final Logger logger = LoggerFactory.getLogger(DiagnosesController.class);

    @PostMapping()
    @Operation(summary = "Crea un nuevo diagnostico", description = "Permite registrar un nuevo diagnostico")
    public ResponseEntity<ApiResponse<DiagnosisResponseDTO>> createDiagnosis(@Valid @RequestBody CreateDiagnosisRequestDTO requestDTO)
    {
        try
        {
            DiagnosisResponseDTO diagnosis = diagnosesService.create(requestDTO);
            ApiResponse<DiagnosisResponseDTO> response =
                new ApiResponse<>
                (
                    200,
                    "Diagnóstico creado",
                    diagnosis
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error al registrar diagnostico: ", e.getMessage());
            ApiResponse<DiagnosisResponseDTO> response =
                new ApiResponse<DiagnosisResponseDTO>
                (
                    400,
                    "Error al registrar diagnostico: " + e.getMessage(),
                    null
                );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
