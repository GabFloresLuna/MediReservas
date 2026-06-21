package cl.duoc.reports.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cl.duoc.reports.dto.CreateGeneratedReportRequestDTO;
import cl.duoc.reports.dto.GeneratedReportResponseDTO;
import cl.duoc.reports.extras.ToDTO;
import cl.duoc.reports.model.GeneratedReport;
import cl.duoc.reports.repository.GeneratedReportRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeneratedReportService {

    private final ToDTO toDTO;
    private final GeneratedReportRepository generatedReportsRepository;

    public GeneratedReportResponseDTO create(CreateGeneratedReportRequestDTO requestDTO) 
    {
        GeneratedReport generatedReport = toDTO.toGeneratedReport(requestDTO);
        generatedReportsRepository.save(generatedReport);
        return toDTO.toGeneratedReportResponseDTO(generatedReport);
    }

    public List<GeneratedReportResponseDTO> listAll() {
        return generatedReportsRepository.findAll()
                .stream()
                .map(x -> toDTO.toGeneratedReportResponseDTO(x))
                .collect(Collectors.toList());
    }
}
