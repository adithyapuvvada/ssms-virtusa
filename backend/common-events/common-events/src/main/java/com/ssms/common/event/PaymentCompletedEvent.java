package com.ssms.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentCompletedEvent {
    private Integer invoiceId;
    private Long shipmentId;
    private double amountPaid;
}