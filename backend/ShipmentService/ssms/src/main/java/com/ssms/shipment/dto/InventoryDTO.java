package com.ssms.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private String itemName;
    private String shipmentCode;
    private Long companyId;
    private int quantity;
    private double unitPrice;
    private Long shipmentId;
}
