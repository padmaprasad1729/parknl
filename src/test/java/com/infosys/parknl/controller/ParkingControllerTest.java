package com.infosys.parknl.controller;

import com.infosys.parknl.model.RegisterDTO;
import com.infosys.parknl.model.UnRegisterDTO;
import com.infosys.parknl.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingControllerTest {

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private ParkingController parkingController;

    @Test
    public void testRegister() {
        // Arrange
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setLicensePlateNumber("TD-210-J");
        registerDTO.setParkingStreet("Java");
        String expectedResponse = "PARKING HAS STARTED AT ";

        when(parkingService.register(registerDTO)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> responseEntity = parkingController.register(registerDTO);

        // Assert
        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(ResponseEntity.ok(expectedResponse), responseEntity);
        verify(parkingService, times(1)).register(registerDTO);
    }

    @Test
    public void testUnregister() {
        // Arrange
        UnRegisterDTO unregisterDTO = new UnRegisterDTO();
        unregisterDTO.setLicensePlateNumber("TD-210-J");
        String expectedResponse = "PARKING SESSION HAS ENDED AT ";

        when(parkingService.unregister(unregisterDTO)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> responseEntity = parkingController.unregister(unregisterDTO);

        // Assert
        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(ResponseEntity.ok(expectedResponse), responseEntity);
        verify(parkingService, times(1)).unregister(unregisterDTO);
    }
}