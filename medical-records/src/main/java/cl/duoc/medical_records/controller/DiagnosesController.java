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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diagnoses")
public class DiagnosesController 
{ 
    private final DiagnosesService diagnosesService; 

    @PostMapping()
    @Operation(summary = "Crear diagnostico", description = "Permite crear diagnostico con id del paciente, descripcion y notas")
    public ResponseEntity<ApiResponse<DiagnosisResponseDTO>> createDiagnosis(@Valid @RequestBody CreateDiagnosisRequestDTO requestDTO)
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
}
