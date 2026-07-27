package com.ssms.shipment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false,unique = true)
        private String name;

        @Column(name = "Location")
        private String location;

        @Column(nullable = false)
        private Double totalCapacity;

        @Column(nullable = false)
        private Double usedCapacity = 0.0;

        @Enumerated(EnumType.STRING)
        private WarehouseStatus status;

        @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
        private List<Shipment> shipments;

        // Helper method
        public Double getAvailableCapacity() {
            return totalCapacity - usedCapacity;
    }

}
