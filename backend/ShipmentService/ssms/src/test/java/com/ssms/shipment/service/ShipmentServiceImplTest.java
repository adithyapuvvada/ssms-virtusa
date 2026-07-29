package com.ssms.shipment.service;

import com.ssms.shipment.dto.InventoryDTO;
import com.ssms.shipment.entity.Shipment;
import com.ssms.shipment.entity.ShipmentStatus;
import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import com.ssms.shipment.exception.ResourceNotFoundException;
import com.ssms.shipment.kafka.producer.ShipmentEventProducer;
import com.ssms.common.event.ShipmentCompletedEvent;
import com.ssms.shipment.openfeign.UserClient;
import com.ssms.shipment.repository.ShipmentRepository;
import com.ssms.shipment.repository.WarehouseRepository;
import com.ssms.shipment.service.impl.ShipmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private UserClient shipperController;

    @Mock
    private ShipmentEventProducer shipmentEventProducer;

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    private Shipment shipment;
    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        shipment = new Shipment();
        shipment.setId(1L);
        shipment.setCompanyId(10L);
        shipment.setVolume(50);
        shipment.setShipmentCode("SHIP-001");
        shipment.setDescription("Sample shipment");

        warehouse = new Warehouse();
        warehouse.setId(100L);
        warehouse.setStatus(WarehouseStatus.ACTIVE);
        warehouse.setTotalCapacity(200.0);
        warehouse.setUsedCapacity(100.0);
    }

    // =============================
    // addIncomingShipment()
    // =============================

    @Test
    void shouldThrowExceptionWhenShipperInvalid() {
        when(shipperController.validateShipper("ROLE_ADMIN",10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> shipmentService.addIncomingShipment(shipment,"ROLE_ADMIN"));

        verify(shipmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNoWarehouseHasCapacity() {
        when(shipperController.validateShipper("ROLE_ADMIN",10L)).thenReturn(true);
        warehouse.setTotalCapacity(100.0);
        warehouse.setUsedCapacity(95.0); // Not enough
        when(warehouseRepository.findByStatus(WarehouseStatus.ACTIVE))
                .thenReturn(List.of(warehouse));

        assertThrows(ResourceNotFoundException.class,
                () -> shipmentService.addIncomingShipment(shipment,"ROLE_ADMIN"));
    }

    @Test
    void shouldStoreShipmentSuccessfully() {
        when(shipperController.validateShipper("ROLE_ADMIN",10L)).thenReturn(true);
        when(warehouseRepository.findByStatus(WarehouseStatus.ACTIVE))
                .thenReturn(List.of(warehouse));
        when(warehouseRepository.save(any())).thenReturn(warehouse);
        when(shipmentRepository.save(any())).thenReturn(shipment);

        Shipment result = shipmentService.addIncomingShipment(shipment,"ROLE_ADMIN");

        assertEquals(ShipmentStatus.STORED, result.getStatus());
        assertNotNull(result.getArrivalDate());

        verify(warehouseRepository, times(1)).save(warehouse);
        verify(shipmentRepository, times(2)).save(shipment);
    }

    @Test
    void shouldMarkWarehouseFullWhenCapacityZero() {
        warehouse.setTotalCapacity(150.0);
        warehouse.setUsedCapacity(100.0); // equal to shipment volume

        when(shipperController.validateShipper("ROLE_ADMIN",10L)).thenReturn(true);
        when(warehouseRepository.findByStatus(WarehouseStatus.ACTIVE))
                .thenReturn(List.of(warehouse));
        when(warehouseRepository.save(any())).thenReturn(warehouse);
        when(shipmentRepository.save(any())).thenReturn(shipment);

        shipmentService.addIncomingShipment(shipment,"ROLE_ADMIN");

        assertEquals(WarehouseStatus.FULL, warehouse.getStatus());
    }

    // =============================
    // addInventory()
    // =============================

    @Test
    void shouldCallInventoryControllerWithCorrectData() {
        shipmentService.addInventory(shipment);

        ArgumentCaptor<ShipmentCompletedEvent> captor =
                ArgumentCaptor.forClass(ShipmentCompletedEvent.class);

        verify(shipmentEventProducer).sendShipmentCreatedEvent(captor.capture());

        ShipmentCompletedEvent captured = captor.getValue();

        assertEquals("Sample shipment", captured.getItemName());
        assertEquals("SHIP-001", captured.getShipmentCode());
        assertEquals(10L, captured.getCompanyId());
        assertEquals(50, captured.getQuantity());
        assertEquals(5.0, captured.getUnitPrice());
        assertEquals(1L, captured.getShipmentId());
    }

    // =============================
    // dispatchShipment()
    // =============================

    @Test
    void shouldThrowExceptionWhenShipmentNotFound() {
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> shipmentService.dispatchShipment(1L));
    }

    @Test
    void shouldDispatchShipmentSuccessfully() {
        shipment.setWarehouse(warehouse);

        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.of(shipment));
        when(warehouseRepository.save(any())).thenReturn(warehouse);
        when(shipmentRepository.save(any())).thenReturn(shipment);

        Shipment result = shipmentService.dispatchShipment(1L);

        assertEquals(ShipmentStatus.DISPATCHED, result.getStatus());
        assertNotNull(result.getDispatchDate());

        verify(warehouseRepository, times(1)).save(warehouse);
        verify(shipmentRepository, times(1)).save(shipment);
    }

    @Test
    void shouldChangeWarehouseStatusToActiveWhenWasFull() {
        warehouse.setStatus(WarehouseStatus.FULL);
        shipment.setWarehouse(warehouse);

        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.of(shipment));
        when(warehouseRepository.save(any())).thenReturn(warehouse);
        when(shipmentRepository.save(any())).thenReturn(shipment);

        shipmentService.dispatchShipment(1L);

        assertEquals(WarehouseStatus.ACTIVE, warehouse.getStatus());
    }

    // =============================
    // getAllShipments()
    // =============================

    @Test
    void shouldReturnAllShipments() {
        when(shipmentRepository.findAll())
                .thenReturn(List.of(shipment));

        List<Shipment> result = shipmentService.getAllShipments("ROLE_ADMIN",1L);

        assertEquals(1, result.size());
        verify(shipmentRepository).findAll();
    }

    // =============================
    // findById()
    // =============================

    @Test
    void shouldReturnShipmentWhenFound() {
        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.of(shipment));

        Shipment result = shipmentService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenShipmentNotFoundById() {
        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> shipmentService.findById(1L));
    }
}