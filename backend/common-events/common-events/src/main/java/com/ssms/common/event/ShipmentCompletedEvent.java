package com.ssms.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentCompletedEvent {
    private String itemName;
    private String shipmentCode;
    private Long companyId;
    private int quantity;
    private double unitPrice;
    private Long shipmentId;
}