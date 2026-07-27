package com.ssms.billing.repository;

import com.ssms.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
    List<Invoice> findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findByStatus(String status);
    List<Invoice> findByCompanyId(Long companyId);

    List<Invoice> findByStatusAndCompanyId(String status, Long companyId);
}
