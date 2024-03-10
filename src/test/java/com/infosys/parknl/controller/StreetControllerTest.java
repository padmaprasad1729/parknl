package com.infosys.parknl.controller;

import com.infosys.parknl.model.StreetDetails;
import com.infosys.parknl.service.StreetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StreetControllerTest {

    @Mock
    private StreetService streetService;

    @InjectMocks
    private StreetController streetController;

    @Test
    public void testAddStreetDetails() {
        // Arrange
        StreetDetails streetDetails = getStreetDetails();
        when(streetService.addStreetDetails(streetDetails)).thenReturn(streetDetails);

        // Act
        ResponseEntity<StreetDetails> responseEntity = streetController.addStreetDetails(streetDetails);

        // Assert
        assertEquals(ResponseEntity.ok(streetDetails), responseEntity);
        verify(streetService, times(1)).addStreetDetails(streetDetails);
    }

    private StreetDetails getStreetDetails() {
        return StreetDetails.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
    }

    @Test
    public void testReadAllStreetDetails() {
        // Arrange
        List<StreetDetails> streetDetailsList = Collections.singletonList(getStreetDetails());
        when(streetService.readAllStreetDetails()).thenReturn(streetDetailsList);

        // Act
        ResponseEntity<List<StreetDetails>> responseEntity = streetController.readAllStreetDetails();

        // Assert
        assertEquals(ResponseEntity.ok(streetDetailsList), responseEntity);
        verify(streetService, times(1)).readAllStreetDetails();
    }

    @Test
    public void testReadStreetDetails() {
        // Arrange
        String streetName = "ExampleStreet"; // Add necessary details
        StreetDetails streetDetails = getStreetDetails();
        when(streetService.findByStreetName(streetName)).thenReturn(streetDetails);

        // Act
        ResponseEntity<StreetDetails> responseEntity = streetController.readStreetDetails(streetName);

        // Assert
        assertEquals(ResponseEntity.ok(streetDetails), responseEntity);
        verify(streetService, times(1)).findByStreetName(streetName);
    }

    @Test
    public void testUpdateStreetDetails() {
        // Arrange
        StreetDetails streetDetails = getStreetDetails();
        when(streetService.updateStreetDetails("Java", streetDetails)).thenReturn(streetDetails);

        // Act
        ResponseEntity<StreetDetails> responseEntity = streetController.updateStreetDetails("Java", streetDetails);

        // Assert
        assertEquals(ResponseEntity.ok(streetDetails), responseEntity);
        verify(streetService, times(1)).updateStreetDetails("Java", streetDetails);
    }

    @Test
    public void testRemoveStreetDetails() {
        // Arrange
        String streetName = "Java";

        // Act
        streetController.removeStreetDetails(streetName);

        // Assert
        verify(streetService, times(1)).removeStreet(streetName);
    }
}