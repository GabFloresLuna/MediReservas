package cl.duoc.medical_records.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.VitalSigns;
import cl.duoc.medical_records.repository.VitalSignsRepository;

public class VitalSignsServiceTest {
    @Test
    void testFindById()
    {
        Long vitalSignId = 1L;
        VitalSignsRepository vitalSignsRepository = Mockito.mock(VitalSignsRepository.class);
        MedicalVisitService medicalVisitService = Mockito.mock(MedicalVisitService.class);
        ToDTO toDTO = Mockito.mock(ToDTO.class);

        VitalSignsService vitalSignsService = new VitalSignsService(vitalSignsRepository, medicalVisitService, toDTO);

        VitalSigns vitalSigns = new VitalSigns();
        vitalSigns.setHeartRate(80);

        VitalSignResponseDTO dto = new VitalSignResponseDTO
        (
            1L,
            1L,
            new BigDecimal(100),
            "PRESIÓN SANGUINEA DEL SISTEMA",
            80,
            new BigDecimal(60),
            new BigDecimal(1.57),
            LocalDateTime.now()
        );

        Mockito.when(vitalSignsRepository.findById(vitalSignId)).thenReturn(Optional.of(vitalSigns));
        Mockito.when(toDTO.toVitalSignResponseDTO(vitalSigns)).thenReturn(dto);
        
        VitalSignResponseDTO result = vitalSignsService.findById(vitalSignId);

        assertThat(result.heartRate()).isEqualTo(80);
    }
}
