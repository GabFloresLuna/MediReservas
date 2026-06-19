package cl.duoc.medical_records.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;

import cl.duoc.medical_records.client.UsersClient;
import cl.duoc.medical_records.dto.DiagnosisResponseDTO;
import cl.duoc.medical_records.dto.MedicalRecordDetailResponseDTO;
import cl.duoc.medical_records.dto.MedicalVisitDetailReponseDTO;
import cl.duoc.medical_records.dto.VitalSignResponseDTO;
import cl.duoc.medical_records.extras.ToDTO;
import cl.duoc.medical_records.model.MedicalRecord;
import cl.duoc.medical_records.model.MedicalVisit;
import cl.duoc.medical_records.repository.MedicalRecordRepository;
import cl.duoc.medical_records.repository.MedicalVisitRepository;

public class MedicalRecordServiceTest {
    @Test
    void testListAll()
    {
        // Initialization of required arguments for a MedicalRecordService instance
        UsersClient usersClient = Mockito.mock(UsersClient.class);
        MedicalRecordRepository medicalRecordRepository = Mockito.mock(MedicalRecordRepository.class);
        MedicalVisitRepository medicalVisitRepository = Mockito.mock(MedicalVisitRepository.class);
        ToDTO toDTO = Mockito.mock(ToDTO.class);
        //Construction of MedicalRecordService Instance
        MedicalRecordService medicalRecordService = new MedicalRecordService(usersClient, medicalRecordRepository, medicalVisitRepository, toDTO);

        //Initialization of required arguments for a MedicalRecord instance
        LocalDateTime createdAt = LocalDateTime.now();
        MedicalVisit medicalVisit = new MedicalVisit();
        List<MedicalVisit> medicalVisits = new ArrayList<>();
        medicalVisits.add(medicalVisit);
        //Construction of a MedicalRecord instance
        MedicalRecord medicalRecord = new MedicalRecord
        (
            1L,
            1L,
            true,
            createdAt,
            medicalVisits
        );
        
        List<MedicalVisitDetailReponseDTO> medicalVisitResponseDTOs = new ArrayList<>();
        List<DiagnosisResponseDTO> diagnosisResponseDTOs = new ArrayList<>();
        List<VitalSignResponseDTO> vitalSignResponseDTOs = new ArrayList<>();
        MedicalVisitDetailReponseDTO medicalVisitDetailReponseDTO = new MedicalVisitDetailReponseDTO
        (
            1L,
            1L,
            1L,
            LocalDateTime.now(),
            "RAZÓN DEL SISTEMA",
            "OBSERVACIONES DEL SISTEMA",
             "TRATAMIENTO DEL SISTEMA",
             LocalDateTime.now(),
             diagnosisResponseDTOs,
             vitalSignResponseDTOs
        );
        medicalVisitResponseDTOs.add(medicalVisitDetailReponseDTO);
        MedicalRecordDetailResponseDTO dto = new MedicalRecordDetailResponseDTO(1L, 1L, true, LocalDateTime.now(),medicalVisitResponseDTOs);

        //Mock configuration
        Mockito.when(medicalRecordRepository.findAll()).thenReturn(List.of(medicalRecord));
        Mockito.when(toDTO.toMedicalRecordDetailResponseDTO(medicalRecord)).thenReturn(dto);

        //Test the function
        List<MedicalRecordDetailResponseDTO> result = medicalRecordService.listAll();

        //Verification of the result
        assertThat(result).hasSize(1);
        assertThat(result.get(0).active()).isEqualTo(true);
    }
}
