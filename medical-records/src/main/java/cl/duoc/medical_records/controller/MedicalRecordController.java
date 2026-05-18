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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController 
{
    private final MedicalRecordService medicalRecordService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<MedicalRecordDetailResponseDTO>>> getAllMedicalRecords()
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

    @PostMapping()
    public ResponseEntity<ApiResponse<MedicalRecordResponseDTO>> createMedicalRecord(@Valid @RequestBody CreateMedicalRecordRequestDTO requestDTO )
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
    
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<MedicalRecordDetailResponseDTO>> getById(@PathVariable Long id)
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
}
