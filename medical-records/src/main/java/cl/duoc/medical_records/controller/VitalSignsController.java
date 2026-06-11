package cl.duoc.medical_records.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.medical_records.dto.ApiResponse;
import cl.duoc.medical_records.dto.CreateVitalSignRequestDTO; 
import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.service.VitalSignsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vital-signs")
public class VitalSignsController 
{

    private final VitalSignsService vitalSignsService;
    private static final Logger logger = LoggerFactory.getLogger(VitalSignsController.class);
   
    @PostMapping()
    @Operation(summary = "Registra signos vitales", description = "Permite registrar los signos vitales relacionados al ID de visita médica")
    public ResponseEntity<ApiResponse<VitalSignResponseDTO>> createVitalSings(@Valid @RequestBody CreateVitalSignRequestDTO requestDTO)
    {
        try
        {
            VitalSignResponseDTO vitalSign = vitalSignsService.create(requestDTO);
            ApiResponse<VitalSignResponseDTO> response =
                new ApiResponse<>
                (
                    200,
                    "Signos vitales creado",
                    vitalSign
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            logger.error("Error al registrar signos vitales: {}", e.getMessage());
            ApiResponse<VitalSignResponseDTO> response =
                new ApiResponse<>
                (
                    400,
                    "Error al registrar signos vitales: " + e.getMessage(),
                    null
                );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
