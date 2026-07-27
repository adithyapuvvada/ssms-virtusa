package com.ssms.shipment.repository;

import com.ssms.shipment.entity.Warehouse;
import com.ssms.shipment.entity.WarehouseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByStatus(WarehouseStatus status);
}