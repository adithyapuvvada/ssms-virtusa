package com.ssms.billing.service;

import com.ssms.billing.dto.InventoryDTO;
import com.ssms.billing.dto.InvoiceDTO;
import com.ssms.billing.entity.Inventory;
import com.ssms.billing.exceptions.ResourceNotFoundException;
import com.ssms.billing.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InvoiceService invoiceService;

    public Inventory addInventory(InventoryDTO inventoryDTO){

        Inventory inventory = new Inventory();
        inventory.setItemName(inventoryDTO.getItemName());
        inventory.setShipmentCode(inventoryDTO.getShipmentCode());
        inventory.setCompanyId(inventoryDTO.getCompanyId());
        inventory.setQuantity(inventoryDTO.getQuantity());
        inventory.setUnitPrice(inventoryDTO.getUnitPrice());
        inventory.setShipmentId(inventoryDTO.getShipmentId());

        inventory.setTotalValue(inventory.getQuantity()*inventory.getUnitPrice());
        inventoryRepository.save(inventory);

        InvoiceDTO invoice = new InvoiceDTO();
        invoice.setCompanyId(inventory.getCompanyId());
        invoice.setShipmentId(inventory.getShipmentId());
        invoice.setCustomerName(inventory.getItemName());
        invoice.setAmount(inventory.getTotalValue());
        invoice.setCurrencyCode("INR");
        invoiceService.createInvoice(invoice);

        return inventory;
    }

    public List<Inventory> getAllInventory(String role,Long companyId){
        List<Inventory> inventoryList;

        if(role.equals("ROLE_SUPPLIER")){
            inventoryList = inventoryRepository.findByCompanyId(companyId);
        }
        else {
            inventoryList = inventoryRepository.findAll();
        }

        if(inventoryList.isEmpty()){
            throw new ResourceNotFoundException("there are no inventories");
        }
        return inventoryList;
    }

    public Inventory getByShipmentId(Long shipmentId){
        Inventory inventory = inventoryRepository.findByShipmentId(shipmentId);
        if(inventory == null){
            throw new ResourceNotFoundException("there is no inventory with shipmentId "+shipmentId);
        }
        return inventory;
    }
}
