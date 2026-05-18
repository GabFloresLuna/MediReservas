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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-visit")
public class MedicalVisitController 
{

    private final MedicalVisitService medicalVisitService;

    @PostMapping()
    public ResponseEntity<ApiResponse<MedicalVisitResponseDTO>> createMedicalVisit(@Valid @RequestBody CreateMedicalVisitRequestDTO requestDTO)
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
}
