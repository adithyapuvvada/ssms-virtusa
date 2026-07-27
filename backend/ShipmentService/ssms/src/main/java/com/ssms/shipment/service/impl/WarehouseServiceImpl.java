package com.ssms.shipment.service.impl;

import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import com.ssms.shipment.exception.ResourceNotFoundException;
import com.ssms.shipment.repository.WarehouseRepository;
import com.ssms.shipment.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    @Override
    @Transactional
    public Warehouse createWarehouse(Warehouse warehouse) {
        warehouse.setUsedCapacity(0.0);
        warehouse.setStatus(WarehouseStatus.ACTIVE);
        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        List<Warehouse> warehouseList= warehouseRepository.findAll();
        if(warehouseList.isEmpty()){
            throw new ResourceNotFoundException("No warehouse exist");
        }
        return warehouseList;
    }
}
