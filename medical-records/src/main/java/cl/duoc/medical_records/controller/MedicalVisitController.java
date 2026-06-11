package cl.duoc.medical_records.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.medical_records.dto.ApiResponse;
import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO;
import cl.duoc.medical_records.dto.MedicalVisitDetailReponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO;
import cl.duoc.medical_records.service.MedicalVisitService; 
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    @Operation(summary = "Registra una visita médica", description = "Permite registrar una visita médica")
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
            logger.error("Error al crear una visita médica: {}", e.getMessage());
            ApiResponse<MedicalVisitResponseDTO> response =
                new ApiResponse<>
                (
                    400,
                    "Error al crear una visita médica: " + e.getMessage()),
                    null
                );
            return ResponseEntity.badRequest().body(response);

        }

    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene visitas médicas por ID del paciente", description = "Permite obtener TODAS las visitas medicas asociadas al ID del paciente")
    public ResponseEntity<ApiResponse<List<MedicalVisitDetailReponseDTO>>> getAllById(@PathVariable Long id) 
    {
        try
        {
            List<MedicalVisitDetailReponseDTO> medicalVisits = medicalVisitService.findAllById(id);
            ApiResponse<List<MedicalVisitDetailReponseDTO>> response =
                new ApiResponse<>
                (
                    200,
                    "Visita(s) Médica(s) encontrada(s)",
                    medicalVisits
                );
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            
        }
    }
    
}
