package cl.duoc.reports.dto;

import java.time.LocalDateTime;

public record GeneratedReportResponseDTO
(
    Long generatedReportId,
    Long reportRequestId,
    Long generatedByUserId,
    String reportType,
    LocalDateTime generatedAt,
    String reportFormat,
    String filePath,
    String reportStatus
)
{}
