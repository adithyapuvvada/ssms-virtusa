package com.ssms.billing.service;

import com.ssms.billing.dto.InvoiceDTO;
import com.ssms.billing.entity.Invoice;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public Invoice createInvoice(InvoiceDTO invoiceDTO){

        System.out.println(invoiceDTO.toString());

        Invoice invoice = new Invoice();
        invoice.setShipmentId(invoiceDTO.getShipmentId());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setCustomerName(invoiceDTO.getCustomerName());
        invoice.setCompanyId(invoiceDTO.getCompanyId());
        invoice.setCurrencyCode(invoiceDTO.getCurrencyCode());
        invoice.setInvoiceNumber("INV-"+ UUID.randomUUID().toString().substring(0,8));
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setStatus("UNPAID");

        System.out.println(invoice.getShipmentId()+" "+invoice.getAmount()+" "+invoice.getCustomerName()+" "+invoice.getCompanyId()+" "
        +invoice.getCurrencyCode()+" "+invoice.getInvoiceNumber()+" "+invoice.getInvoiceDate()+" "+invoice.getStatus());

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices(String role,Long companyId){
        List<Invoice> invoiceList;

        if(role.equals("ROLE_SUPPLIER"))
            invoiceList = invoiceRepository.findByCompanyId(companyId);
        else
            invoiceList = invoiceRepository.findAll();

        if(invoiceList.isEmpty()){
            throw new ResourceNotFoundException("there are no invoices inside");
        }
        return invoiceList;
    }

    public Invoice getInvoiceById(Integer id){
        return invoiceRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Invoice "+id+" not found"));
    }

    public void markAsPaid(Integer id){
        Invoice invoice = getInvoiceById(id);
        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);
    }
}
