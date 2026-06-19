package cl.duoc.medical_records.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.medical_records.dto.ApiResponse;
import cl.duoc.medical_records.dto.IdVerificationRequestDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DoctorsClient {
    private final WebClient.Builder webClientBuilder;

    public Boolean doctorIdVerification(Long doctorId) {
        try {
            ApiResponse<Boolean> response = webClientBuilder.build()
                    .patch()
                    .uri(
                            "http://doctors-service/api/v2/doctors/{id}",
                            doctorId
                    )
                    .bodyValue(new IdVerificationRequestDTO(doctorId))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {})
                    .block();
            return response != null && Boolean.TRUE.equals(response.getData());

        } catch (WebClientResponseException.NotFound ex) {
            throw new RuntimeException(
                    "ID de Paciente no encontrado"
            );


        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Error al asignar el verificar la existencia de ID del Paciente"
            );
        }
    }
}
