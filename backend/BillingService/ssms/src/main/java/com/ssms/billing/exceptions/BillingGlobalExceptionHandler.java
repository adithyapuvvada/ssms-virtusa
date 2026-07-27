package com.ssms.billing.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;

@RestControllerAdvice
public class BillingGlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResouceEx(ResourceNotFoundException exception){
        return new ResponseEntity<>(
                new ErrorResponse(
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        LocalDate.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException exception){
        return new ResponseEntity<>(
                new ErrorResponse(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDate.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(PaymentMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePaymentMismatch(PaymentMismatchException exception){
        return new ResponseEntity<>(
                new ErrorResponse(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDate.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleInvalidEnum(HttpMessageNotReadableException ex){
        return ResponseEntity.badRequest()
                .body("Invalid payment mode. Available modes are: UPI, CASH, CARD");
    }
}
