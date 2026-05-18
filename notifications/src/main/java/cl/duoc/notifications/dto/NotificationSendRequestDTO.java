package cl.duoc.notifications.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationSendRequestDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    @NotBlank(message = "El canal es obligatorio")
    @Size(max = 30, message = "El canal debe tener máximo 30 caracteres")
    private String notificationChannel;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título debe tener máximo 100 caracteres")
    private String notificationTitle;

    @NotBlank(message = "El mensaje es obligatorio")
    private String notificationMessage;

    private Long notificationTemplateId;
}
