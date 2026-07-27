package com.ssms.shipment.service;

import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import com.ssms.shipment.repository.WarehouseRepository;
import com.ssms.shipment.service.impl.WarehouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Main Warehouse");
        warehouse.setTotalCapacity(200.0);
        warehouse.setUsedCapacity(50.0); // initial value, will be overwritten
        warehouse.setStatus(WarehouseStatus.FULL); // initial, will be overwritten
    }

    // =============================
    // createWarehouse()
    // =============================
    @Test
    void shouldCreateWarehouseWithZeroUsedCapacityAndActiveStatus() {
        // Simulate save returns the warehouse object
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        Warehouse result = warehouseService.createWarehouse(warehouse);

        // Verify business logic
        assertEquals(0.0, result.getUsedCapacity());
        assertEquals(WarehouseStatus.ACTIVE, result.getStatus());

        // Verify repository save called with correct warehouse
        ArgumentCaptor<Warehouse> captor = ArgumentCaptor.forClass(Warehouse.class);
        verify(warehouseRepository).save(captor.capture());

        Warehouse savedWarehouse = captor.getValue();
        assertEquals(0.0, savedWarehouse.getUsedCapacity());
        assertEquals(WarehouseStatus.ACTIVE, savedWarehouse.getStatus());
    }

    // =============================
    // getAllWarehouses()
    // =============================
    @Test
    void shouldReturnAllWarehouses() {
        when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));

        List<Warehouse> result = warehouseService.getAllWarehouses();

        assertEquals(1, result.size());
        assertEquals("Main Warehouse", result.get(0).getName());

        verify(warehouseRepository, times(1)).findAll();
    }
}