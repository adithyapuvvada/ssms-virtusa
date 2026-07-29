package com.ssms.billing.controller;


import com.ssms.billing.dto.PaymentDTO;
import com.ssms.billing.entity.Payment;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ssms/billing/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/pay")
    public Payment makePayment(@RequestHeader(value="X-User-Role", required=false) String role,
                               @RequestBody PaymentDTO payment){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return paymentService.makePayment(payment);
    }

    @GetMapping("/invoice/{invoiceId}")
    public Payment getPayments(@RequestHeader(value="X-User-Role", required=false) String role,
                               @PathVariable("invoiceId") int invoiceId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return paymentService.getPaymentsByInvoice(invoiceId);
    }

    @GetMapping
    public List<Payment> getAllPayments(@RequestHeader(value="X-User-Role", required=false) String role,Long companyId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return paymentService.getAllPayments(role,companyId);
    }

    private boolean isAuthorized(String userRole, String... allowedRoles) {
        if (userRole == null) return false;
        for (String allowed : allowedRoles) {
            if (allowed.contains(userRole)) return true;
        }
        return false;
    }
}
