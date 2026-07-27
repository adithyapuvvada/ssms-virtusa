package com.ssms.shipment.repository;

import com.ssms.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
    List<Shipment> findByCompanyId(Long companyId);
}
