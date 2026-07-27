package com.ssms.billing.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private String itemName;
    private String shipmentCode;
    private Long companyId;
    private int quantity;
    private double unitPrice;
    private Long shipmentId;
}
