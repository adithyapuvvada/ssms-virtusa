package com.ssms.billing.controller;

import com.ssms.billing.entity.Invoice;
import com.ssms.billing.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReportService reportService;

    @Test
    @WithMockUser
    void shouldReturnPaidInvoices() throws Exception {

        when(reportService.getPaidInvoices("", 1L))
                .thenReturn(List.of(new Invoice(), new Invoice()));

        mockMvc.perform(get("/ssms/billing/reports/paid"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnUnpaidInvoices() throws Exception {

        when(reportService.getUnPaidInvoices("UNPAID",1L))
                .thenReturn(List.of(new Invoice()));

        mockMvc.perform(get("/ssms/billing/reports/unpaid"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnRevenue() throws Exception {

        when(reportService.getTotalRevenue())
                .thenReturn(8000.0);

        mockMvc.perform(get("/ssms/billing/reports/revenue"))
                .andExpect(status().isOk())
                .andExpect(content().string("Total Revenue = 8000.0"));
    }
}
