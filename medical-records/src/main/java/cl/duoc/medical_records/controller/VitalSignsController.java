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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vital-signs")
public class VitalSignsController 
{

    private final VitalSignsService vitalSignsService;
   
    @PostMapping()
    public ResponseEntity<ApiResponse<VitalSignResponseDTO>> createVitalSings(@Valid @RequestBody CreateVitalSignRequestDTO requestDTO)
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
}
