package com.ssms.userservice.service;

import com.ssms.userservice.dto.ShipperDTO;
import com.ssms.userservice.entity.Shipper;
import com.ssms.userservice.exception.ResourceNotFoundException;
import com.ssms.userservice.repository.ShipperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipperServiceTest {

    @Mock
    private ShipperRepository shipperRepository;

    @InjectMocks
    private ShipperService shipperService;

    @Test
    void testGetById_Success() {

        Shipper shipper = new Shipper(1L, "Tata", "tata@gmail.com", 1234567890L);

        when(shipperRepository.findById(1L))
                .thenReturn(Optional.of(shipper));

        Shipper result = shipperService.getById(1L);

        assertEquals("Tata", result.getCompanyName());
        verify(shipperRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {

        when(shipperRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            shipperService.getById(99L);
        });

        verify(shipperRepository, times(1)).findById(99L);
    }

    @Test
    void testAddShipper_Success() {

        // Arrange
        ShipperDTO shipperDTO = new ShipperDTO();
        shipperDTO.setCompanyName("TCS");
        shipperDTO.setEmail("tcs@gmail.com");
        shipperDTO.setPhone(1234567890L);

        Shipper shipper = new Shipper();
        shipper.setCompanyName("TCS");
        shipper.setEmail("tcs@gmail.com");
        shipper.setPhone(1234567890L);

        when(shipperRepository.findByEmail("tcs@gmail.com"))
                .thenReturn(Optional.empty());

        when(shipperRepository.save(any(Shipper.class)))
                .thenReturn(shipper);

        // Act
        Shipper saved = shipperService.addShipper(shipperDTO);

        // Assert
        assertEquals("TCS", saved.getCompanyName());
        assertEquals("tcs@gmail.com", saved.getEmail());
        assertEquals(1234567890, saved.getPhone());

        verify(shipperRepository, times(1)).save(any(Shipper.class));
    }
    @Test
    void testAddShipper_DuplicateEmail() {

        ShipperDTO shipper = new ShipperDTO();
        shipper.setEmail("tcs@gmail.com");

        when(shipperRepository.findByEmail("tcs@gmail.com"))
                .thenReturn(Optional.of(new Shipper()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shipperService.addShipper(shipper);
        });

        assertEquals("Email already exists", exception.getMessage());
    }
}