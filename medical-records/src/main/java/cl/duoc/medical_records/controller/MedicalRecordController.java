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
import cl.duoc.medical_records.dto.CreateDiagnosisRequestDTO;
import cl.duoc.medical_records.dto.CreateMedicalRecordRequestDTO;
import cl.duoc.medical_records.dto.CreateMedicalVisitRequestDTO;
import cl.duoc.medical_records.dto.CreateVitalSignRequestDTO;
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordDetailResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordResponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitResponseDTO;
import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.service.DiagnosesService;
import cl.duoc.medical_records.service.MedicalRecordService;
import cl.duoc.medical_records.service.MedicalVisitService;
import cl.duoc.medical_records.service.VitalSignsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController 
{
    private final MedicalRecordService medicalRecordService;
    private final DiagnosesService diagnosesService;
    private final VitalSignsService vitalSignsService;
    private final MedicalVisitService medicalVisitService;

    @GetMapping("/list")
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

    @GetMapping("patient/{id}")
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

    @PostMapping("/diagnosis")
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

    @PostMapping("/vital-signs")
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

    @PostMapping("/medical-visits")
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
