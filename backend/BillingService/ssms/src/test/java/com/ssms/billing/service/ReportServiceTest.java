package com.ssms.billing.service;

import com.ssms.billing.entity.Invoice;
import com.ssms.billing.repository.InvoiceRepository;
import com.ssms.billing.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private InvoiceRepository invoiceRepo;

    @Mock
    private PaymentRepository paymentRepo;

    @InjectMocks
    private ReportService reportService;

    @Test
    void shouldReturnPaidInvoices() {

        when(invoiceRepo.findByStatus("PAID"))
                .thenReturn(List.of(new Invoice(), new Invoice()));

        List<Invoice> result = reportService.getPaidInvoices("", 1L);

        assertEquals(2, result.size());
        verify(invoiceRepo).findByStatus("PAID");
    }

    @Test
    void shouldReturnUnpaidInvoices() {

        when(invoiceRepo.findByStatus("UNPAID"))
                .thenReturn(List.of(new Invoice()));

        List<Invoice> result = reportService.getUnPaidInvoices("",1l);

        assertEquals(1, result.size());
        verify(invoiceRepo).findByStatus("UNPAID");
    }

    @Test
    void shouldReturnTotalRevenue() {

        when(paymentRepo.getTotalRevenue())
                .thenReturn(5000.0);

        Double revenue = reportService.getTotalRevenue();

        assertEquals(5000.0, revenue);
        verify(paymentRepo).getTotalRevenue();
    }
}
