package cl.duoc.medical_records.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.Diagnoses;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.DiagnosesRepository;

public class DiagnosesServiceTest {

    @Test
    void testFindById()
    {
        Long diagnosisId = 1L;
        DiagnosesRepository diagnosesRepository = Mockito.mock(DiagnosesRepository.class);
        MedicalVisitService medicalVisitService = Mockito.mock(MedicalVisitService.class);
        ToDTO toDTO = Mockito.mock(ToDTO.class);
        DiagnosesService diagnosesService = new DiagnosesService(diagnosesRepository, medicalVisitService, toDTO);

        MedicalVisit medicalVisit = new MedicalVisit();
        medicalVisit.setId(1L);
        Diagnoses diagnoses = new Diagnoses(1L, medicalVisit, "DIAGNOSTICO DEL SISTEMA", "NOTAS DEL SISTEMA");

        DiagnosisResponseDTO dto = new DiagnosisResponseDTO(1L, 1L, "DIAGNOSTICO DEL SISTEMA", "NOTAS DEL SISTEMA");

        Mockito.when(diagnosesRepository.findById(diagnosisId)).thenReturn(Optional.of(diagnoses));
        Mockito.when(toDTO.toDiagnosisResponseDTO(diagnoses)).thenReturn(dto);

        DiagnosisResponseDTO result = diagnosesService.findById(diagnosisId);

        assertThat(result.medicalVisitId()).isEqualTo(medicalVisit.getId());
    }
}
