package com.ssms.shipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import com.ssms.shipment.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WarehouseController.class)
class WareHouseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WarehouseService warehouseService;

    @Autowired
    private ObjectMapper objectMapper;

    // ----------- Normal cases -----------

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void testCreateWarehouse() throws Exception {
        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .name("Main Warehouse")
                .location("New York")
                .totalCapacity(1000.0)
                .usedCapacity(0.0)
                .status(WarehouseStatus.ACTIVE)
                .build();

        Mockito.when(warehouseService.createWarehouse(any(Warehouse.class))).thenReturn(warehouse);

        mockMvc.perform(post("/ssms/shipment/warehouses")
                        .header("X-User-Role", "ROLE_MANAGER")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Main Warehouse")))
                .andExpect(jsonPath("$.location", is("New York")))
                .andExpect(jsonPath("$.totalCapacity", is(1000.0)))
                .andExpect(jsonPath("$.usedCapacity", is(0.0)))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void testGetAllWarehouses() throws Exception {
        Warehouse w1 = Warehouse.builder()
                .id(1L)
                .name("Warehouse 1")
                .location("Los Angeles")
                .totalCapacity(500.0)
                .usedCapacity(100.0)
                .status(WarehouseStatus.ACTIVE)
                .build();

        Warehouse w2 = Warehouse.builder()
                .id(2L)
                .name("Warehouse 2")
                .location("Chicago")
                .totalCapacity(800.0)
                .usedCapacity(200.0)
                .status(WarehouseStatus.ACTIVE)
                .build();

        List<Warehouse> warehouses = Arrays.asList(w1, w2);
        Mockito.when(warehouseService.getAllWarehouses()).thenReturn(warehouses);

        mockMvc.perform(get("/ssms/shipment/warehouses")
                        .header("X-User-Role", "ROLE_MANAGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Warehouse 1")))
                .andExpect(jsonPath("$[1].name", is("Warehouse 2")));
    }

    // ----------- Edge cases -----------

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void testGetAllWarehousesEmpty() throws Exception {
        Mockito.when(warehouseService.getAllWarehouses()).thenReturn(List.of());

        mockMvc.perform(get("/ssms/shipment/warehouses")
                        .header("X-User-Role", "ROLE_MANAGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void testCreateWarehouseInvalidInput() throws Exception {
        Warehouse invalidWarehouse = Warehouse.builder()
                .totalCapacity(1000.0)
                .usedCapacity(0.0)
                .build(); // Missing required 'name' and 'location'

        mockMvc.perform(post("/ssms/shipment/warehouses")
                        .header("X-User-Role", "ROLE_MANAGER")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidWarehouse)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "manager1", roles = {"MANAGER"})
    void testCreateWarehouseServiceException() throws Exception {
        Warehouse warehouse = Warehouse.builder()
                .name("Faulty Warehouse")
                .location("Nowhere")
                .totalCapacity(500.0)
                .usedCapacity(0.0)
                .build();

        Mockito.when(warehouseService.createWarehouse(any(Warehouse.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/ssms/shipment/warehouses")
                        .header("X-User-Role", "ROLE_MANAGER")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(warehouse)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Database error")));
    }
}
