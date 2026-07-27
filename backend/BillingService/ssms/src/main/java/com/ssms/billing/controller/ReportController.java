package com.ssms.billing.controller;

import com.ssms.billing.entity.Invoice;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ssms/billing/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/paid")
    public List<Invoice> getPaidInvoices(@RequestHeader(value="X-User-Role", required=false) String role, Long companyId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return reportService.getPaidInvoices(role, companyId);
    }

    @GetMapping("/unpaid")
    public List<Invoice> getUnPaidInvoices(@RequestHeader(value="X-User-Role", required=false) String role,Long companyId){
        if(!isAuthorized(role,"ROLE_SUPPLIER","ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        return reportService.getUnPaidInvoices(role,companyId);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String,Double>> getTotalRevenue(@RequestHeader(value="X-User-Role", required=false) String role){
        if(!isAuthorized(role,"ROLE_ACCOUNTANT","ROLE_ADMIN")){
            throw new ResourceNotFoundException("Access Denied");
        }
        Map<String,Double> response = new HashMap<>();
        response.put("totalRevenue",reportService.getTotalRevenue());
        return ResponseEntity.ok(response);
    }

    private boolean isAuthorized(String userRole, String... allowedRoles) {
        for (String allowed : allowedRoles) {
            if (allowed.contains(userRole)) return true;
        }
        return false;
    }
}
