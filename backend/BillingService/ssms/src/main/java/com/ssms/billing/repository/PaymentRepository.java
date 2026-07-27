package com.ssms.billing.repository;

import com.ssms.billing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    Payment findByInvoiceId(Integer invoiceId);
    List<Payment> findByCompanyId(Long companyId);

    @Query("select sum(p.amountPaid) from Payment p")
    Double getTotalRevenue();
}
