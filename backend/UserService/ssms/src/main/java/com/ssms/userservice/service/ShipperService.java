package com.ssms.userservice.service;

import com.ssms.userservice.dto.ShipperDTO;
import com.ssms.userservice.entity.Shipper;
import com.ssms.userservice.exception.ResourceNotFoundException;
import com.ssms.userservice.exception.ShipperException;
import com.ssms.userservice.repository.ShipperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipperService {

    private final ShipperRepository shipperRepository;


    public Shipper addShipper(ShipperDTO shipperDTO) {
        System.out.println(shipperDTO.toString());
        Shipper shipper = new Shipper();

        shipper.setCompanyName(shipperDTO.getCompanyName());
        shipper.setEmail(shipperDTO.getEmail());
        shipper.setPhone(shipperDTO.getPhone());
        shipper.setCountry(shipperDTO.getCountry());
        shipper.setCurrencyCode(shipperDTO.getCurrencyCode());

        System.out.println(shipper.getId()+" "+shipper.getCompanyName()+" "+shipper.getPhone()+" "+shipper.getCountry()+" "+shipper.getCurrencyCode());
        // Check duplicate email
        if (shipperRepository.findByEmail(shipper.getEmail()).isPresent()) {
            throw new ShipperException("Email already exists");
        }

        return shipperRepository.save(shipper);
    }

    public List<Shipper> getAllShippers() {
        List<Shipper> shipperList = shipperRepository.findAll();
        if(shipperList.isEmpty()){
            throw new ResourceNotFoundException("there are no shippers available");
        }
        return shipperList;
    }

    public void deleteShipper(Long id) {
        getById(id);
        shipperRepository.deleteById(id);
    }

    public Shipper getById(Long id) {
        return shipperRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipper not found with id: " + id));
    }

}