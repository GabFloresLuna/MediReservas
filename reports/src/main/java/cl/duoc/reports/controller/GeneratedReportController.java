package cl.duoc.reports.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.reports.dto.ApiResponse;
import cl.duoc.reports.dto.CreateGeneratedReportRequestDTO;
import cl.duoc.reports.dto.GeneratedReportResponseDTO;
import cl.duoc.reports.service.GeneratedReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/generated-reports")
public class GeneratedReportController 
{
    private final GeneratedReportService generatedReportService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<GeneratedReportResponseDTO>>> getAllGeneratedReport()
    {
        List<GeneratedReportResponseDTO> generatedReports = generatedReportService.listAll();
        ApiResponse<List<GeneratedReportResponseDTO>> response =
            new ApiResponse<>
            (
                200,
                "Listado de reportes generados",
                generatedReports
            );
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<GeneratedReportResponseDTO>> createGeneratedReport(@Valid @RequestBody CreateGeneratedReportRequestDTO requestDTO)
    {
        GeneratedReportResponseDTO generatedReport = generatedReportService.create(requestDTO);
        ApiResponse<GeneratedReportResponseDTO> response =
            new ApiResponse<>
            (
                200,
                "Reporte generado creado",
                generatedReport
            );
        return ResponseEntity.ok(response);
    }
}
