package com.infosys.parknl.service;

import com.infosys.parknl.exception.StreetAlreadyFoundException;
import com.infosys.parknl.exception.StreetNotFoundException;
import com.infosys.parknl.model.StreetDetails;
import com.infosys.parknl.model.StreetDetailsDAO;
import com.infosys.parknl.repository.StreetDetailsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StreetServiceTest {

    @Mock
    private StreetDetailsRepository streetRepo;

    @Mock
    private SequenceGeneratorService seqService;

    @InjectMocks
    private StreetService streetService;

    @Test
    public void testAddStreetDetails() {
        // Arrange
        StreetDetails streetDetails = getStreetDetails("Java", BigDecimal.valueOf(10));
        StreetDetailsDAO streetDetailsDAO = getStreetDetailsDAO("Java", BigDecimal.valueOf(10));
        when(seqService.generateSequence(StreetDetailsDAO.SEQUENCE_NAME)).thenReturn(1L);
        when(streetRepo.save(any(StreetDetailsDAO.class))).thenReturn(streetDetailsDAO);
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.empty());

        // Act
        StreetDetails result = streetService.addStreetDetails(streetDetails);

        // Assert
        assertEquals(streetDetails, result);
        verify(seqService, times(1)).generateSequence(StreetDetailsDAO.SEQUENCE_NAME);
        verify(streetRepo, times(1)).save(any(StreetDetailsDAO.class));
    }

    private StreetDetails getStreetDetails(String street, BigDecimal price) {
        return StreetDetails.builder()
                .streetName(street)
                .priceInCentPerMinute(price)
                .build();
    }

    private StreetDetailsDAO getStreetDetailsDAO(String street, BigDecimal price) {
        return StreetDetailsDAO.builder()
                .streetName(street)
                .priceInCentPerMinute(price)
                .build();
    }

    @Test
    public void testAddStreetDetails_StreetAlreadyFoundException() {
        // Arrange
        StreetDetails streetDetails = getStreetDetails("Java", BigDecimal.valueOf(10));
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.of(getStreetDetailsDAO("Java", BigDecimal.valueOf(15))));

        // Act & Assert
        assertThrows(StreetAlreadyFoundException.class, () -> streetService.addStreetDetails(streetDetails));
    }

    @Test
    public void testReadAllStreetDetails() {
        // Arrange
        StreetDetailsDAO streetDetailsDAO = getStreetDetailsDAO("Java", BigDecimal.valueOf(15));
        when(streetRepo.findAll()).thenReturn(Collections.singletonList(streetDetailsDAO));

        // Act
        StreetDetails result = streetService.readAllStreetDetails().get(0);

        // Assert
        assertEquals(streetDetailsDAO.getStreetName(), result.getStreetName());
        assertEquals(streetDetailsDAO.getPriceInCentPerMinute(), result.getPriceInCentPerMinute());
    }

    @Test
    public void testFindByStreetName() {
        // Arrange
        StreetDetailsDAO streetDetailsDAO = getStreetDetailsDAO("Java", BigDecimal.valueOf(15));
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.of(streetDetailsDAO));

        // Act
        StreetDetails result = streetService.findByStreetName("Java");

        // Assert
        assertEquals(streetDetailsDAO.getStreetName(), result.getStreetName());
        assertEquals(streetDetailsDAO.getPriceInCentPerMinute(), result.getPriceInCentPerMinute());
    }

    @Test
    public void testFindByStreetName_StreetNotFoundException() {
        // Arrange
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StreetNotFoundException.class, () -> streetService.findByStreetName("Java"));
    }

    @Test
    public void testUpdateStreetDetails() {
        // Arrange
        StreetDetailsDAO existingStreetDetailsDAO = getStreetDetailsDAO("Java", BigDecimal.valueOf(10));
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.of(existingStreetDetailsDAO));

        StreetDetails streetDetails = getStreetDetails("Jakarta", BigDecimal.valueOf(15));
        StreetDetailsDAO newStreetDetailsDAO = getStreetDetailsDAO("Jakarta", BigDecimal.valueOf(15));
        when(streetRepo.save(any(StreetDetailsDAO.class))).thenReturn(newStreetDetailsDAO);

        // Act
        StreetDetails result = streetService.updateStreetDetails("Java", streetDetails);

        // Assert
        assertEquals(streetDetails, result);
        verify(streetRepo, times(1)).save(any(StreetDetailsDAO.class));
    }

    @Test
    public void testUpdateStreetDetails_wontUpdateIfSame() {
        // Arrange
        StreetDetails streetDetails = StreetDetails.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
        StreetDetailsDAO existingStreetDetailsDAO = StreetDetailsDAO.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.of(existingStreetDetailsDAO));

        // Act
        StreetDetails result = streetService.updateStreetDetails("Java", streetDetails);

        // Act & Assert
        assertEquals(streetDetails, result);
    }

    @Test
    public void testUpdateStreetDetails_StreetAlreadyFoundException() {
        // Arrange
        StreetDetails streetDetails = StreetDetails.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
        StreetDetailsDAO existingStreetDetailsDAO = StreetDetailsDAO.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
        when(streetRepo.findByStreetName("Java")).thenThrow(new StreetAlreadyFoundException(""));

        // Act & Assert
        assertThrows(StreetAlreadyFoundException.class, () -> streetService.updateStreetDetails("Java", streetDetails));
    }

    @Test
    public void testRemoveStreet() {
        // Arrange
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.of(getStreetDetailsDAO("Java", BigDecimal.valueOf(15))));

        // Act
        assertDoesNotThrow(() -> streetService.removeStreet("Java"));

        // Assert
        verify(streetRepo, times(1)).delete(any(StreetDetailsDAO.class));
    }

    @Test
    public void testRemoveStreet_StreetNotFoundException() {
        // Arrange
        when(streetRepo.findByStreetName("Java")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StreetNotFoundException.class, () -> streetService.removeStreet("Java"));
    }
}