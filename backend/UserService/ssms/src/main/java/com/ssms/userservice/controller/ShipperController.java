package com.ssms.userservice.controller;

import com.ssms.userservice.dto.ShipperDTO;
import com.ssms.userservice.entity.Shipper;
import com.ssms.userservice.service.ShipperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ssms/userservice/shippers")
@RequiredArgsConstructor

public class ShipperController {

    private final ShipperService shipperService;

    @PostMapping
    public Shipper addShipper(@RequestHeader(value="X-User-Role", required=false) String role,
                              @Valid @RequestBody ShipperDTO shipper) {
        return shipperService.addShipper(shipper);
    }

    @GetMapping
    public List<Shipper> getAllShippers(@RequestHeader(value="X-User-Role", required=false) String role) {
        return shipperService.getAllShippers();
    }

    @DeleteMapping("/{id}")
    public String deleteShipper(@RequestHeader(value="X-User-Role", required=false) String role,
                                @PathVariable Long id) {
        shipperService.deleteShipper(id);
        return "Shipper deleted successfully";
    }

    @GetMapping("/validate/{id}")
    public Boolean validateShipper(@RequestHeader(value="X-User-Role", required=false) String role,
                                   @PathVariable Long id) {
        shipperService.getById(id);   // if not found → exception thrown
        return true;
    }
}