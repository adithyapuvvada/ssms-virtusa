package com.ssms.billing.service;

import com.ssms.billing.dto.PaymentDTO;
import com.ssms.billing.entity.Invoice;
import com.ssms.billing.entity.Payment;
import com.ssms.billing.exceptions.InvalidOperationException;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.openfeign.ShipmentClient;
import com.ssms.billing.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private ShipmentClient shipmentService;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldMakePaymentSuccessfully(){

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setInvoiceId(1);
        paymentDTO.setAmountPaid(1000.0);

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1);
        invoice.setAmount(1000.0);
        invoice.setShipmentId(10L);

        when(invoiceService.getInvoiceById(1)).thenReturn(invoice);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.makePayment(paymentDTO);

        assertNotNull(result.getPaymentReference());
        assertEquals(LocalDate.now(), result.getPaymentDate());

        verify(shipmentService).dispatchShipment(10L);
        verify(invoiceService).markAsPaid(1);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void shouldThrowExceptionWhenAmountIsZero() {

        PaymentDTO payment = new PaymentDTO();
        payment.setAmountPaid(0);

        assertThrows(InvalidOperationException.class,
                () -> paymentService.makePayment(payment));
    }

    @Test
    void shouldThrowExceptionWhenAmountMismatch() {

        PaymentDTO payment = new PaymentDTO();
        payment.setInvoiceId(1);
        payment.setAmountPaid(500.0);

        Invoice invoice = new Invoice();
        invoice.setAmount(1000.0);

        when(invoiceService.getInvoiceById(1)).thenReturn(invoice);

        assertThrows(Exception.class,
                () -> paymentService.makePayment(payment));
    }

    @Test
    void shouldReturnPaymentByInvoiceId() {

        Payment payment = new Payment();
        payment.setInvoiceId(1);

        when(paymentRepository.findByInvoiceId(1))
                .thenReturn(payment);

        Payment result = paymentService.getPaymentsByInvoice(1);

        assertEquals(1, result.getInvoiceId());
    }

    @Test
    void shouldThrowWhenPaymentNotFound() {

        when(paymentRepository.findByInvoiceId(5))
                .thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.getPaymentsByInvoice(5));
    }
}
