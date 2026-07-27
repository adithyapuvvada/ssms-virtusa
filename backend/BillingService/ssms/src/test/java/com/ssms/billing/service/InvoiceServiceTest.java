package com.ssms.billing.service;

import com.ssms.billing.dto.InvoiceDTO;
import com.ssms.billing.entity.Invoice;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void shouldCreateInvoiceWithGeneratedFields(){
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCustomerName("adithya");
        invoiceDTO.setAmount(1000);

        when(invoiceRepository.save(any(Invoice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Invoice result = invoiceService.createInvoice(invoiceDTO);

        assertNotNull(result.getInvoiceNumber());
        assertEquals("UNPAID", result.getStatus());
        assertNotNull(result.getInvoiceDate());
        assertEquals("adithya", result.getCustomerName());
        assertEquals(1000, result.getAmount());

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void shouldReturnAllInvoices(){
        when(invoiceRepository.findAll())
                .thenReturn(List.of(new Invoice(),new Invoice()));

        List<Invoice> invoiceList = invoiceRepository.findAll();
        System.out.println(invoiceList);
        assertEquals(2,invoiceList.size());
        verify(invoiceRepository).findAll();
    }

    @Test
    void shouldReturnGetInvoiceById() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1);

        when(invoiceRepository.findById(1))
                .thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.getInvoiceById(1);

        assertEquals(1,result.getInvoiceId());
    }

    @Test
    void shouldThrowExceptionWhenInvoiceNotFound(){
        when(invoiceRepository.findById(5))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                ()->invoiceService.getInvoiceById(5));
    }

    @Test
    void shouldMarkInvoiceAsPaid() {

        Invoice invoice = new Invoice();
        invoice.setInvoiceId(1);
        invoice.setStatus("UNPAID");

        when(invoiceRepository.findById(1))
                .thenReturn(Optional.of(invoice));

        invoiceService.markAsPaid(1);

        assertEquals("PAID", invoice.getStatus());
        verify(invoiceRepository).save(invoice);
    }
}
