package cl.duoc.payments.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cl.duoc.payments.dto.PaymentDTO;
import cl.duoc.payments.dto.PaymentReceiptDTO;
import cl.duoc.payments.dto.RefundDTO;
import cl.duoc.payments.model.Payment;
import cl.duoc.payments.model.PaymentReceipt;
import cl.duoc.payments.model.Refund;
import cl.duoc.payments.repository.PaymentReceiptRepository;
import cl.duoc.payments.repository.PaymentRepository;
import cl.duoc.payments.repository.RefundRepository;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentReceiptRepository paymentReceiptRepository;
    private final RefundRepository refundRepository;

    public PaymentService(PaymentRepository paymentRepository, 
                          PaymentReceiptRepository paymentReceiptRepository,
                          RefundRepository refundRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentReceiptRepository = paymentReceiptRepository;
        this.refundRepository = refundRepository;
    }

    // --- OBTENER TODOS LOS PAGOS ---
    public List<PaymentDTO> findAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    // --- OBTENER PAGO POR ID ---
    public PaymentDTO findPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        return convertToPaymentDTO(payment);
    }

    // --- OBTENER PAGOS POR APPOINTMENT ID ---
    public List<PaymentDTO> findPaymentsByAppointmentId(Long appointmentId) {
        return paymentRepository.findByAppointmentId(appointmentId).stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    // --- OBTENER PAGOS POR PATIENT USER ID ---
    public List<PaymentDTO> findPaymentsByPatientUserId(Long patientUserId) {
        return paymentRepository.findByPatientUserId(patientUserId).stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    // --- OBTENER PAGOS POR ESTADO ---
    public List<PaymentDTO> findPaymentsByStatus(String status) {
        return paymentRepository.findByPaymentStatus(status).stream()
                .map(this::convertToPaymentDTO)
                .toList();
    }

    // --- CREAR NUEVO PAGO ---
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setAppointmentId(paymentDTO.getAppointmentId());
        payment.setPatientUserId(paymentDTO.getPatientUserId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus("PENDING");
        payment.setTransactionCode(generateTransactionCode());
        payment.setCreatedAt(LocalDateTime.now());
        
        Payment savedPayment = paymentRepository.save(payment);
        return convertToPaymentDTO(savedPayment);
    }

    // --- ACTUALIZAR ESTADO DE PAGO (Ej: COMPLETADO, FALLIDO) ---
    @Transactional
    public PaymentDTO updatePaymentStatus(Long id, String status, String transactionCode) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        payment.setPaymentStatus(status);
        
        if ("COMPLETED".equals(status)) {
            payment.setPaidAt(LocalDateTime.now());
        }
        
        if (transactionCode != null && !transactionCode.isEmpty()) {
            payment.setTransactionCode(transactionCode);
        }
        
        return convertToPaymentDTO(paymentRepository.save(payment));
    }

    // --- GENERAR COMPROBANTE DE PAGO ---
    @Transactional
    public PaymentReceiptDTO generateReceipt(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        // Verificar si ya existe un comprobante para este pago
        if (paymentReceiptRepository.findByPayment_PaymentId(paymentId).isPresent()) {
            throw new RuntimeException("Ya existe un comprobante para este pago");
        }
        
        // Verificar que el pago esté completado
        if (!"COMPLETED".equals(payment.getPaymentStatus())) {
            throw new RuntimeException("Solo se pueden generar comprobantes para pagos completados");
        }
        
        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setPayment(payment);
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setIssuedAt(LocalDateTime.now());
        receipt.setTotalAmount(payment.getAmount());
        
        PaymentReceipt savedReceipt = paymentReceiptRepository.save(receipt);
        return convertToReceiptDTO(savedReceipt);
    }

    // --- OBTENER COMPROBANTE POR ID DE PAGO ---
    public PaymentReceiptDTO getReceiptByPaymentId(Long paymentId) {
        PaymentReceipt receipt = paymentReceiptRepository.findByPayment_PaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Comprobante no encontrado para este pago"));
        return convertToReceiptDTO(receipt);
    }

    // --- SOLICITAR REEMBOLSO ---
    @Transactional
    public RefundDTO requestRefund(Long paymentId, BigDecimal refundAmount, String reason) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        // Validar que el pago esté completado
        if (!"COMPLETED".equals(payment.getPaymentStatus())) {
            throw new RuntimeException("Solo se pueden reembolsar pagos completados");
        }
        
        // Validar que el monto de reembolso no exceda el monto del pago
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new RuntimeException("El monto de reembolso no puede exceder el monto del pago");
        }
        
        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setRefundAmount(refundAmount);
        refund.setRefundReason(reason);
        refund.setRefundStatus("PENDING");
        refund.setCreatedAt(LocalDateTime.now());
        
        Refund savedRefund = refundRepository.save(refund);
        
        // Actualizar estado del pago a REFUNDING
        payment.setPaymentStatus("REFUNDING");
        paymentRepository.save(payment);
        
        return convertToRefundDTO(savedRefund);
    }

    // --- ACTUALIZAR ESTADO DE REEMBOLSO ---
    @Transactional
    public RefundDTO updateRefundStatus(Long refundId, String status) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Reembolso no encontrado"));
        
        refund.setRefundStatus(status);
        Refund savedRefund = refundRepository.save(refund);
        
        // Si el reembolso fue completado, actualizar estado del pago
        if ("COMPLETED".equals(status)) {
            Payment payment = refund.getPayment();
            payment.setPaymentStatus("REFUNDED");
            paymentRepository.save(payment);
        }
        
        return convertToRefundDTO(savedRefund);
    }

    // --- OBTENER REEMBOLSOS POR PAGO ---
    public List<RefundDTO> getRefundsByPaymentId(Long paymentId) {
        return refundRepository.findByPayment_PaymentId(paymentId).stream()
                .map(this::convertToRefundDTO)
                .toList();
    }

    // --- ELIMINAR PAGO (Solo si está pendiente) ---
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        
        // Solo permitir eliminar pagos pendientes
        if (!"PENDING".equals(payment.getPaymentStatus())) {
            throw new RuntimeException("Solo se pueden eliminar pagos en estado PENDING");
        }
        
        paymentRepository.delete(payment);
    }

    // --- MÉTODOS AUXILIARES ---
    
    private String generateTransactionCode() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateReceiptNumber() {
        return "RCP-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    
    // --- MÉTODOS DE CONVERSIÓN ---
    
    private PaymentDTO convertToPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAppointmentId(payment.getAppointmentId());
        dto.setPatientUserId(payment.getPatientUserId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setTransactionCode(payment.getTransactionCode());
        dto.setPaidAt(payment.getPaidAt());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
    
    private PaymentReceiptDTO convertToReceiptDTO(PaymentReceipt receipt) {
        PaymentReceiptDTO dto = new PaymentReceiptDTO();
        dto.setPaymentReceiptId(receipt.getPaymentReceiptId());
        dto.setPaymentId(receipt.getPayment().getPaymentId());
        dto.setReceiptNumber(receipt.getReceiptNumber());
        dto.setIssuedAt(receipt.getIssuedAt());
        dto.setTotalAmount(receipt.getTotalAmount());
        return dto;
    }
    
    private RefundDTO convertToRefundDTO(Refund refund) {
        RefundDTO dto = new RefundDTO();
        dto.setRefundId(refund.getRefundId());
        dto.setPaymentId(refund.getPayment().getPaymentId());
        dto.setRefundAmount(refund.getRefundAmount());
        dto.setRefundReason(refund.getRefundReason());
        dto.setRefundStatus(refund.getRefundStatus());
        dto.setCreatedAt(refund.getCreatedAt());
        return dto;
    }
}