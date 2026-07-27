package com.ssms.billing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssms.billing.entity.Inventory;
import com.ssms.billing.service.InventoryService;
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

@WebMvcTest(InventoryController.class)
@WithMockUser(roles = "INVENTORY_MANAGER")
class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllInventory() throws Exception{
        when(inventoryService.getAllInventory("ROLE_ADMIN",1l))
                .thenReturn(List.of(new Inventory(),new Inventory()));

        mockMvc.perform(get("/ssms/billing/inventory/all"))
                .andExpect(status().isOk());
    }
}
