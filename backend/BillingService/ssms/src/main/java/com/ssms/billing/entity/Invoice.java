package com.ssms.billing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invoiceId;

    private String invoiceNumber;
    private Long companyId;
    private Long shipmentId;
    private String customerName;
    private double amount;
    private LocalDate invoiceDate;
    private String status;
    @Column(name = "currency_code")
    private String currencyCode;
}
