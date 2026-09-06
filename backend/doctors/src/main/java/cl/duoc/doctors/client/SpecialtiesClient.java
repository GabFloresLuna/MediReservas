package cl.duoc.doctors.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.doctors.dto.ApiResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SpecialtiesClient {

    private final WebClient.Builder webClientBuilder;

    public void validateSpecialty(Long specialtyId) {

        boolean exists = getBoolean(
                "http://specialties-service/api/v1/specialties/{specialtyId}/exists",
                specialtyId
        );

        if (!exists) {
            throw new RuntimeException(
                    "Especialidad no encontrada con ID: " + specialtyId
            );
        }

        boolean active = getBoolean(
                "http://specialties-service/api/v1/specialties/{specialtyId}/active",
                specialtyId
        );

        if (!active) {
            throw new RuntimeException(
                    "La especialidad está inactiva. ID: " + specialtyId
            );
        }
    }

    private boolean getBoolean(String uri, Long specialtyId) {
        ApiResponse<Boolean> response =
                webClientBuilder.build()
                        .get()
                        .uri(uri, specialtyId)
                        .retrieve()
                        .bodyToMono(
                                new ParameterizedTypeReference<
                                        ApiResponse<Boolean>
                                >() {}
                        )
                        .block();

        return response != null
                && Boolean.TRUE.equals(response.getData());
    }
}