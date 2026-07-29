package com.ssms.shipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssms.shipment.dto.ShipmentRequestDTO;
import com.ssms.shipment.entity.Shipment;
import com.ssms.shipment.entity.ShipmentStatus;
import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import com.ssms.shipment.exception.ResourceNotFoundException;
import com.ssms.shipment.service.ShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShipmentService shipmentService;

    private Shipment shipment;

    @BeforeEach
    void setUp() {
        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .name("Warehouse A")
                .location("Location A")
                .totalCapacity(100.0)
                .usedCapacity(20.0)
                .status(WarehouseStatus.ACTIVE)
                .build();

        shipment = Shipment.builder()
                .id(1L)
                .shipmentCode("SHIP-001")
                .companyId(10L)
                .description("Sample shipment")
                .volume(50)
                .status(ShipmentStatus.STORED)
                .arrivalDate(LocalDateTime.now())
                .warehouse(warehouse)
                .build();

        // Mock the service call if needed
        when(shipmentService.addIncomingShipment(any(Shipment.class),"ROLE_ADMIN"))
                .thenReturn(shipment);
    }

    @Test
    @WithMockUser(username = "supplierUser", roles = {"SUPPLIER"})
    void shouldReturnAllShipments() throws Exception {
        when(shipmentService.getAllShipments("ROLE_ADMIN",1L)).thenReturn(List.of(shipment));

        mockMvc.perform(get("/ssms/shipment/shipments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(shipment.getId()))
                .andExpect(jsonPath("$[0].shipmentCode").value(shipment.getShipmentCode()));
    }

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void shouldReturnShipmentById() throws Exception {
        when(shipmentService.findById(1L)).thenReturn(shipment);

        mockMvc.perform(get("/ssms/shipment/shipments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shipment.getId()))
                .andExpect(jsonPath("$.shipmentCode").value(shipment.getShipmentCode()));
    }

    @Test
    @WithMockUser(username = "inventoryUser", roles = {"INVENTORY_MANAGER"})
    void shouldReturnNotFoundWhenShipmentNotFound() throws Exception {
        when(shipmentService.findById(1L))
                .thenThrow(new ResourceNotFoundException("Shipment not found"));

        mockMvc.perform(get("/ssms/shipment/shipments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Shipment not found"));
    }

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void shouldAddShipmentSuccessfully() throws Exception {
        ShipmentRequestDTO requestDTO = new ShipmentRequestDTO();
        requestDTO.setCompanyId(10L);
        requestDTO.setDescription("Sample shipment");
        requestDTO.setVolume(50);

        when(shipmentService.addIncomingShipment(any(Shipment.class),"ROLE_ADMIN")).thenReturn(shipment);
        Mockito.doNothing().when(shipmentService).addInventory(any(Shipment.class));

        mockMvc.perform(post("/ssms/shipment/shipments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shipment.getId()))
                .andExpect(jsonPath("$.shipmentCode").value(shipment.getShipmentCode()));
    }

    @Test
    @WithMockUser(username = "supplierUser", roles = {"SUPPLIER"})
    void shouldReturnBadRequestForInvalidShipmentRequest() throws Exception {
        ShipmentRequestDTO requestDTO = new ShipmentRequestDTO(); // all fields null/invalid

        mockMvc.perform(post("/ssms/shipment/shipments")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

}