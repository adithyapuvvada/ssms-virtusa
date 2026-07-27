package com.ssms.billing.exceptions;

public class PaymentMismatchException extends RuntimeException {
    public PaymentMismatchException(String message) {
        super(message);
    }
}
