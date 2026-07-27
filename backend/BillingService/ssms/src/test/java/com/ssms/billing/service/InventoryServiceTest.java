package com.ssms.billing.service;

import com.ssms.billing.dto.InventoryDTO;
import com.ssms.billing.dto.InvoiceDTO;
import com.ssms.billing.entity.Inventory;
import com.ssms.billing.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void shouldAddInventoryAndCreateInvoice() {

        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setShipmentId(1L);
        inventoryDTO.setItemName("Laptop");
        inventoryDTO.setQuantity(2);
        inventoryDTO.setUnitPrice(500.0);

        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Inventory result = inventoryService.addInventory(inventoryDTO);

        assertEquals(1000.0, result.getTotalValue());
        System.out.println("expected = 1000 || result = "+result.getTotalValue());
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
        verify(invoiceService, times(1)).createInvoice(any(InvoiceDTO.class));
    }

    @Test
    void shouldReturnAllInventory() {

        when(inventoryRepository.findAll())
                .thenReturn(List.of(new Inventory(), new Inventory()));

        List<Inventory> list = inventoryService.getAllInventory("ROLE_ADMIN",1L);

        assertEquals(2, list.size());
        System.out.println("expected = 2 || result = "+list.size());
        verify(inventoryRepository).findAll();
    }

    @Test
    void shouldReturnInventoryByShipmentId() {

        Inventory inventory = new Inventory();
        inventory.setShipmentId(10L);

        when(inventoryRepository.findByShipmentId(10L))
                .thenReturn(inventory);

        Inventory result = inventoryService.getByShipmentId(10L);

        assertEquals(10L, result.getShipmentId());
        System.out.println("expected = 10 || result = "+result.getShipmentId());
        verify(inventoryRepository).findByShipmentId(10L);
    }
}
