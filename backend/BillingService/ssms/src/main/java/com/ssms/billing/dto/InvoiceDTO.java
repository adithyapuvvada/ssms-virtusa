package com.ssms.billing.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long shipmentId;
    private Long companyId;
    private String customerName;
    private double amount;
    private String currencyCode;
}
