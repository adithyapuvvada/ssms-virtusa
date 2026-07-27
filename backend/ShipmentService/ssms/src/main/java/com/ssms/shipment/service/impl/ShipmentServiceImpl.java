package com.ssms.shipment.service.impl;

import com.ssms.shipment.entity.Shipment;
import com.ssms.shipment.entity.ShipmentStatus;
import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import com.ssms.common.event.ShipmentCompletedEvent;
import com.ssms.shipment.exception.ResourceNotFoundException;
import com.ssms.shipment.kafka.producer.ShipmentEventProducer;
import com.ssms.shipment.openfeign.UserClient;
import com.ssms.shipment.repository.ShipmentRepository;
import com.ssms.shipment.repository.WarehouseRepository;
import com.ssms.shipment.service.ShipmentService;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserClient shipperClient;
    private final ShipmentEventProducer shipmentEventProducer;

    @Override
    @Transactional
    @Retry(name = "${spring.application.name}", fallbackMethod = "handleShipperValidationFailure")
    public Shipment addIncomingShipment(Shipment shipment,String role) {
        //verifying company
        if(Boolean.FALSE.equals(shipperClient.validateShipper(role,shipment.getCompanyId()))){
            throw new ResourceNotFoundException("sorry company "+ shipment.getCompanyId()+" is not found");
        }

        List<Warehouse> activeWarehouses = warehouseRepository.findByStatus(WarehouseStatus.ACTIVE);

        Warehouse selectedWarehouse = activeWarehouses.stream()
                .filter(w->w.getAvailableCapacity() >= shipment.getVolume())
                .findFirst().orElseThrow(()->new ResourceNotFoundException("No Warehouse has enough space"));

        shipment.setWarehouse(selectedWarehouse);
        shipment.setArrivalDate(LocalDateTime.now());
        shipment.setStatus(ShipmentStatus.STORED);

        selectedWarehouse.setUsedCapacity(
                selectedWarehouse.getUsedCapacity()+shipment.getVolume()
        );

        if(selectedWarehouse.getAvailableCapacity()<=0){
            selectedWarehouse.setStatus(WarehouseStatus.FULL);
        }

        warehouseRepository.save(selectedWarehouse);

        Shipment saved = shipmentRepository.save(shipment);
        saved.setShipmentCode(String.format("SHIP-%09d", saved.getId()));
        return shipmentRepository.save(saved);
    }

    public Shipment handleShipperValidationFailure(Shipment shipment, String role, Exception e){
        e.printStackTrace();
        throw new ResourceNotFoundException(
                "User service is currently unavailable. Unable to validate shipper. Please try again later."
        );
    }


    @Override
    public void addInventory(Shipment shipment){
        shipmentEventProducer.sendShipmentCreatedEvent(new ShipmentCompletedEvent(
                shipment.getDescription(),
                shipment.getShipmentCode(),
                shipment.getCompanyId(),
                shipment.getVolume(),
                5,
                shipment.getId()
        ));
    }

    @Override
    @Transactional
    public Shipment dispatchShipment(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(()-> new ResourceNotFoundException("Shipment not found with id: "+shipmentId));

        Warehouse warehouse = shipment.getWarehouse();

        warehouse.setUsedCapacity(
                warehouse.getUsedCapacity()-shipment.getVolume()
        );

        if(warehouse.getStatus() == WarehouseStatus.FULL){
            warehouse.setStatus(WarehouseStatus.ACTIVE);
        }

        shipment.setStatus(ShipmentStatus.DISPATCHED);
        shipment.setDispatchDate(LocalDateTime.now());

        warehouseRepository.save(warehouse);
        return shipmentRepository.save(shipment);
    }

    @Override
    public List<Shipment> getAllShipments(String role,Long companyId){
        List<Shipment> shipments;

        if(role.equals("ROLE_SUPPLIER")){
            shipments = shipmentRepository.findByCompanyId(companyId);
        }
        else{
            shipments = shipmentRepository.findAll();
        }
        if (shipments.isEmpty()){
            throw new ResourceNotFoundException("no shipments inside");
        }
        return shipments;
}

    @Override
    public Shipment findById(Long id){
        return shipmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("no shipment found good"));
    }
}
