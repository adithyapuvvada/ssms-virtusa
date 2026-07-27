package com.ssms.userservice.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipperDTO {
    @Column(nullable = false,unique = true)
    private String companyName;

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must be valid (example: user@example.com)"
    )
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @Positive
    @Column(nullable = false)
    @Min(value = 1000000000L, message = "Phone must be 10 digits")
    @Max(value = 9999999999L, message = "Phone must be 10 digits")
    private Long phone;

    // NEW REGIONAL COLUMNS
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;
}