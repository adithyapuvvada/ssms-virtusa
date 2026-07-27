package com.ssms.billing.service;

import com.ssms.billing.entity.Invoice;
import com.ssms.billing.repository.InvoiceRepository;
import com.ssms.billing.repository.PaymentRepository;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;

    public List<Invoice> getPaidInvoices(String role, Long companyId) {
        if ("ROLE_SUPPLIER".equalsIgnoreCase(role)) {
            return invoiceRepo.findByStatusAndCompanyId("PAID", companyId);
        }
        return invoiceRepo.findByStatus("PAID");
    }

    public List<Invoice> getUnPaidInvoices(String role, Long companyId) {
        if (role.equals("ROLE_SUPPLIER")) {
            return invoiceRepo.findByStatusAndCompanyId("UNPAID", companyId);
        }
        return invoiceRepo.findByStatus("UNPAID");
    }

    public Double getTotalRevenue() {
        return paymentRepo.getTotalRevenue();
    }
}
