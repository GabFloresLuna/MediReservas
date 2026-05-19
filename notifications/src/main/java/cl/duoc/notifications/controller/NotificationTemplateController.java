package cl.duoc.notifications.controller;

import cl.duoc.notifications.dto.ApiResponse;
import cl.duoc.notifications.dto.NotificationTemplateCreateRequestDTO;
import cl.duoc.notifications.dto.NotificationTemplateResponseDTO;
import cl.duoc.notifications.service.NotificationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification-templates")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService templateService;

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationTemplateResponseDTO>> createTemplate(
        @Valid @RequestBody NotificationTemplateCreateRequestDTO dto) {
            NotificationTemplateResponseDTO response = templateService.createTemplate(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                    201, 
                    "Plantilla creada con éxito", 
                    response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationTemplateResponseDTO>>> getAllTemplates() {
        List<NotificationTemplateResponseDTO> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(
                new ApiResponse<>(
                    200, 
                    "Plantillas obtenidas exitosamente", 
                    templates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplateResponseDTO>> getTemplateById(
            @PathVariable Long id) {
        NotificationTemplateResponseDTO response = templateService.getTemplateById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                    200, 
                    "Plantilla obtenida con éxito", 
                    response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<NotificationTemplateResponseDTO>>> getActiveTemplates() {
        List<NotificationTemplateResponseDTO> templates = templateService.getActiveTemplates();
        return ResponseEntity.ok(
                new ApiResponse<>(
                    200, 
                    "Plantillas activas obtenidas exitosamente", 
                    templates));
    }
}