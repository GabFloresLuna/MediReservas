package cl.duoc.medical_records.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.medical_records.dto.ApiResponse;
import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO; 
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO;
import cl.duoc.medical_records.service.MedicalVisitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-visit")
public class MedicalVisitController 
{

    private final MedicalVisitService medicalVisitService;
    private static final Logger logger = LoggerFactory.getLogger(MedicalVisitController.class);

    @PostMapping()
    @Operation(summary = "Crea una nueva visita médica", description = "Permite registrar una nueva visita médica")
    public ResponseEntity<ApiResponse<MedicalVisitResponseDTO>> createMedicalVisit(@Valid @RequestBody CreateMedicalVisitRequestDTO requestDTO)
    {
        
        try
        {
            MedicalVisitResponseDTO medicalVisit = medicalVisitService.create(requestDTO);
            ApiResponse<MedicalVisitResponseDTO> response =
                new ApiResponse<>
                (
                    200,
                    "Visita médica creada",
                    medicalVisit
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error al registrar diagnostico: ", e.getMessage());
            ApiResponse<MedicalVisitResponseDTO> response =
                new ApiResponse<MedicalVisitResponseDTO>
                (
                    400,
                    "Error al registrar diagnostico: " + e.getMessage(),
                    null
                );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
