package cl.duoc.medical_records.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentsClient 
{
    private final WebClient.Builder webClientBuilder;

}
