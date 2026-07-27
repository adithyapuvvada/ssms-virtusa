package com.ssms.userservice.repository;

import com.ssms.userservice.entity.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipperRepository extends JpaRepository<Shipper, Long> {

    Optional<Shipper> findByEmail(String email);
}