package com.ssms.billing.dto;

import com.ssms.billing.enums.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    @NotNull(message = "invoice id must not be null")
    private Integer invoiceId;
    private double amountPaid;
    @NotNull(message = "payment mode is required")
    private PaymentMode paymentMode;
}
