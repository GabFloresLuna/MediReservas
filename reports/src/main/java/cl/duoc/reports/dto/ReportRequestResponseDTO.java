package cl.duoc.reports.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReportRequestResponseDTO
(
    Long reportRquestId,
    String reportType,
    Long requestedByUserId,
    LocalDate startDate,
    LocalDate endDate,
    String requestStatus,
    LocalDateTime createdAt
)
{}
