package com.ssms.billing.repository;

import com.ssms.billing.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    Inventory findByShipmentId(Long shipmentId);
    List<Inventory> findByCompanyId(Long companyId);
}
