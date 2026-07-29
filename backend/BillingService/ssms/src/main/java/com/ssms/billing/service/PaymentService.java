package com.ssms.billing.service;

import com.ssms.billing.dto.PaymentDTO;
import com.ssms.billing.entity.Invoice;
import com.ssms.billing.entity.Payment;
import com.ssms.common.event.PaymentCompletedEvent;
import com.ssms.billing.exceptions.InvalidOperationException;
import com.ssms.billing.exceptions.PaymentMismatchException;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.kafka.producer.PaymentEventProducer;
import com.ssms.billing.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;
    private final PaymentEventProducer paymentEventProducer;

    public Payment makePayment(PaymentDTO paymentDTO){

        Payment payment = new Payment();
        payment.setInvoiceId(paymentDTO.getInvoiceId());
        payment.setAmountPaid(paymentDTO.getAmountPaid());
        payment.setPaymentMode(paymentDTO.getPaymentMode().name());

        if(payment.getAmountPaid() <= 0){
            throw new InvalidOperationException("Payment must be greater than 0");
        }
        Invoice invoice = invoiceService.getInvoiceById(payment.getInvoiceId());
        payment.setCompanyId(invoice.getCompanyId());
        if(invoice.getStatus().equals("PAID"))
            throw new InvalidOperationException("already paid");

        if(invoice.getAmount() != payment.getAmountPaid())
            throw new PaymentMismatchException("Payment amount is not same. Please pay " + invoice.getAmount() + " to get your product");

        //kafka event
        System.out.println("sending to kAKFA");
        paymentEventProducer.sendInvoiceCreatedEvent(new PaymentCompletedEvent(
                payment.getInvoiceId(),
                invoice.getShipmentId(),
                payment.getAmountPaid()
        ));

        System.out.println("sent to kafka");
        payment.setPaymentReference("PAY-" + UUID.randomUUID().toString().substring(0,8));
        payment.setPaymentDate(LocalDate.now());
        System.out.println("setting paid in inovic");
        invoiceService.markAsPaid(payment.getInvoiceId());
        System.out.println("reposons to basedn");
        return paymentRepository.save(payment);
    }

    public Payment getPaymentsByInvoice(Integer invoiceId){
        Payment payment = paymentRepository.findByInvoiceId(invoiceId);
        if(payment == null){
            throw new ResourceNotFoundException("Sorry, there is no payment with this invoice id");
        }
        return payment;
    }

    public List<Payment> getAllPayments(String role,Long companyId){
        List<Payment> paymentList;

        if(role.equals("ROLE_SUPPLIER"))
            paymentList = paymentRepository.findByCompanyId(companyId);
        else
            paymentList = paymentRepository.findAll();

        if(paymentList.isEmpty()){
            throw new ResourceNotFoundException("there are no invoices inside");
        }
        return paymentList;
    }
}