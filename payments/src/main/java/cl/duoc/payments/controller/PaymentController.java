package cl.duoc.payments.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.payments.dto.PaymentDTO;
import cl.duoc.payments.dto.PaymentReceiptDTO;
import cl.duoc.payments.dto.RefundDTO;
import cl.duoc.payments.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    
    // GET: Obtener todos los pagos
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.findAllPayments();
        return ResponseEntity.ok(payments);
    }

    // GET: Obtener un pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO payment = paymentService.findPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    // GET: Obtener pagos por appointment ID
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByAppointmentId(@PathVariable Long appointmentId) {
        List<PaymentDTO> payments = paymentService.findPaymentsByAppointmentId(appointmentId);
        return ResponseEntity.ok(payments);
    }

    // GET: Obtener pagos por patient user ID
    @GetMapping("/patient/{patientUserId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByPatientUserId(@PathVariable Long patientUserId) {
        List<PaymentDTO> payments = paymentService.findPaymentsByPatientUserId(patientUserId);
        return ResponseEntity.ok(payments);
    }

    // GET: Obtener pagos por estado
    @GetMapping("/status")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@RequestParam String status) {
        List<PaymentDTO> payments = paymentService.findPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    // POST: Crear un nuevo pago
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    // PUT: Actualizar estado de un pago
    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable Long id, 
                                                          @RequestParam String status,
                                                          @RequestParam(required = false) String transactionCode) {
        PaymentDTO updatedPayment = paymentService.updatePaymentStatus(id, status, transactionCode);
        return ResponseEntity.ok(updatedPayment);
    }

    // DELETE: Eliminar un pago (solo si está pendiente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== RECEIPTS ====================

    // POST: Generar comprobante de pago
    @PostMapping("/{paymentId}/receipt")
    public ResponseEntity<PaymentReceiptDTO> generateReceipt(@PathVariable Long paymentId) {
        PaymentReceiptDTO receipt = paymentService.generateReceipt(paymentId);
        return new ResponseEntity<>(receipt, HttpStatus.CREATED);
    }

    // GET: Obtener comprobante por payment ID
    @GetMapping("/{paymentId}/receipt")
    public ResponseEntity<PaymentReceiptDTO> getReceiptByPaymentId(@PathVariable Long paymentId) {
        PaymentReceiptDTO receipt = paymentService.getReceiptByPaymentId(paymentId);
        return ResponseEntity.ok(receipt);
    }

    // ==================== REFUNDS ====================

    // POST: Solicitar reembolso
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<RefundDTO> requestRefund(@PathVariable Long paymentId,
                                                   @RequestParam java.math.BigDecimal amount,
                                                   @RequestParam String reason) {
        RefundDTO refund = paymentService.requestRefund(paymentId, amount, reason);
        return new ResponseEntity<>(refund, HttpStatus.CREATED);
    }

    // PUT: Actualizar estado de reembolso
    @PutMapping("/refund/{refundId}/status")
    public ResponseEntity<RefundDTO> updateRefundStatus(@PathVariable Long refundId,
                                                        @RequestParam String status) {
        RefundDTO refund = paymentService.updateRefundStatus(refundId, status);
        return ResponseEntity.ok(refund);
    }

    // GET: Obtener reembolsos por payment ID
    @GetMapping("/{paymentId}/refunds")
    public ResponseEntity<List<RefundDTO>> getRefundsByPaymentId(@PathVariable Long paymentId) {
        List<RefundDTO> refunds = paymentService.getRefundsByPaymentId(paymentId);
        return ResponseEntity.ok(refunds);
    }
}