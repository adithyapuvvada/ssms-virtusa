package com.ssms.shipment.service;

import com.ssms.shipment.entity.Warehouse;

import java.util.List;

public interface WarehouseService {
    Warehouse createWarehouse(Warehouse warehouse);
    List<Warehouse> getAllWarehouses();
}
