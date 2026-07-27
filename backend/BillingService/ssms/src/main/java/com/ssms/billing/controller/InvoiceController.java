package com.ssms.billing.controller;

import com.ssms.billing.entity.Invoice;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssms/billing/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/all")
    public List<Invoice> getAllInvoices(@RequestHeader(value="X-User-Role", required=false) String role,Long companyId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return invoiceService.getAllInvoices(role,companyId);
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@RequestHeader(value="X-User-Role", required=false) String role,
                                  @PathVariable("id") int id){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return invoiceService.getInvoiceById(id);
    }

    private boolean isAuthorized(String userRole, String... allowedRoles) {
        for (String allowed : allowedRoles) {
            if (allowed.contains(userRole)) return true;
        }
        return false;
    }
}
