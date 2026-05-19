package cl.duoc.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    
    private Long paymentId;
    private Long appointmentId;
    private Long patientUserId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionCode;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}