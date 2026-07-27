package com.ssms.billing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssms.billing.dto.PaymentDTO;
import com.ssms.billing.entity.Payment;
import com.ssms.billing.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldMakePayment() throws Exception {

        Payment payment = new Payment();
        payment.setInvoiceId(1);

        when(paymentService.makePayment(any(PaymentDTO.class)))
                .thenReturn(payment);

        mockMvc.perform(post("/ssms/billing/payment/pay")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldGetPaymentByInvoiceId() throws Exception {

        Payment payment = new Payment();
        payment.setInvoiceId(1);

        when(paymentService.getPaymentsByInvoice(1))
                .thenReturn(payment);

        mockMvc.perform(get("/ssms/billing/payment/invoice/1"))
                .andExpect(status().isOk());
    }
}
