package com.ssms.billing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssms.billing.entity.Invoice;
import com.ssms.billing.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvoiceController.class)
@WithMockUser(roles = "ACCOUNTANT")
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldReturnAllInvoices() throws Exception {

        when(invoiceService.getAllInvoices("ROLE_ADMIN",1L))
                .thenReturn(List.of(new Invoice(), new Invoice()));

        mockMvc.perform(get("/ssms/billing/invoice/all")
                        .header("X-User-Role", "ROLE_ADMIN")
                        .param("companyId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnInvoiceById() throws Exception {

        Invoice invoice = new Invoice();

        when(invoiceService.getInvoiceById(1))
                .thenReturn(invoice);

        mockMvc.perform(get("/ssms/billing/invoice/1")
                        .header("X-User-Role", "ROLE_ADMIN"))
                .andExpect(status().isOk());
    }
}
