package com.ssms.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssms.userservice.dto.ShipperDTO;
import com.ssms.userservice.entity.Shipper;
import com.ssms.userservice.service.ShipperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipperController.class)
class ShipperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShipperService shipperService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllShippers() throws Exception {

        Shipper shipper = new Shipper(1L, "Tata", "tata@gmail.com", 1234567890L, "India", "INR");

        when(shipperService.getAllShippers())
                .thenReturn(List.of(shipper));

        mockMvc.perform(get("/ssms/userservice/shippers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Tata"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddShipper() throws Exception {

        Shipper shipper = new Shipper(1L, "TCS", "tcs@gmail.com", 1234567890L, "India", "INR");

        when(shipperService.addShipper(any(ShipperDTO.class)))
                .thenReturn(shipper);

        mockMvc.perform(post("/ssms/userservice/shippers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shipper)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("TCS"));
    }
}