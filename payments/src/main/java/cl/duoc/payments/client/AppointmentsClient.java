package cl.duoc.payments.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cl.duoc.payments.dto.ApiResponse;
import cl.duoc.payments.dto.IdVerificationRequestDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentsClient 
{
    private final WebClient.Builder webClientBuilder;

    public Boolean appointmentIdVerification(Long appointmentId) {
        try {
            ApiResponse<Boolean> response = webClientBuilder.build()
                    .patch()
                    .uri(
                            "http://appointments-service/api/v1/appointments/{appointmentId}",
                            appointmentId
                    )
                    .bodyValue(new IdVerificationRequestDTO(appointmentId))
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
