package cl.duoc.reports.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.reports.dto.ApiResponse;
import cl.duoc.reports.dto.CreateReportRequestDTO;
import cl.duoc.reports.dto.ReportRequestResponseDTO;
import cl.duoc.reports.service.ReportRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports-request")
public class ReportRequestController 
{
    private final ReportRequestService reportRequestService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ReportRequestResponseDTO>>> getAllGeneratedReport()
    {
        List<ReportRequestResponseDTO> generatedReports = reportRequestService.listAll();
        ApiResponse<List<ReportRequestResponseDTO>> response =
            new ApiResponse<>
            (
                200,
                "Listado de reportes generados",
                generatedReports
            );
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<ReportRequestResponseDTO>> createGeneratedReport(@Valid @RequestBody CreateReportRequestDTO requestDTO)
    {
        ReportRequestResponseDTO generatedReport = reportRequestService.create(requestDTO);
        ApiResponse<ReportRequestResponseDTO> response =
            new ApiResponse<>
            (
                200,
                "Reporte generado creado",
                generatedReport
            );
        return ResponseEntity.ok(response);
    }
}
