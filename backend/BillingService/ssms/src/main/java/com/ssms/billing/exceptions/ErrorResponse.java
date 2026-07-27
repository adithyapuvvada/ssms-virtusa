package com.ssms.billing.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDate localDate;
}
