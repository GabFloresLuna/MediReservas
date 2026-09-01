package cl.duoc.medical_records;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import cl.duoc.medical_records.client.AppointmentsClient;
import cl.duoc.medical_records.client.DoctorsClient;
import cl.duoc.medical_records.client.UsersClient;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.flyway.enabled=false"
})
public class MedicalRecordsApplicationTest {
    @MockitoBean
    UsersClient usersClient;
    @MockitoBean
    DoctorsClient doctorsClient;
    @MockitoBean
    AppointmentsClient appointmentsClient;

    @Test
    void contextLoads(){}
}
