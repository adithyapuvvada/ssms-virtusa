package com.ssms.shipment.service;

import com.ssms.shipment.entity.Shipment;

import java.util.List;

public interface ShipmentService {
    Shipment addIncomingShipment(Shipment shipment,String role);
    Shipment dispatchShipment(Long shipmentId);
    List<Shipment> getAllShipments(String role,Long companyId);
    Shipment findById(Long id);
    void addInventory(Shipment shipment);
}
