package com.infosys.parknl.service;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.model.ParkingDetailsDAO;
import com.infosys.parknl.model.ParkingMonitorDAO;
import com.infosys.parknl.model.ParkingMonitorData;
import com.infosys.parknl.repository.ParkingDetailsRepository;
import com.infosys.parknl.repository.ParkingMonitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingMonitorServiceTest {

    @Mock
    private ParkingMonitorRepository parkingMonitorRepository;

    @Mock
    private ParkingDetailsRepository parkingDetailsRepository;

    @Mock
    private SequenceGeneratorService seqService;

    @InjectMocks
    private ParkingMonitorService parkingMonitorService;

    @Test
    public void testAddParkingMonitorData() {
        // Arrange
        ParkingMonitorData parkingMonitorData = getParkingMonitorData();

        // Act
        parkingMonitorService.addParkingMonitorData(Collections.singletonList(parkingMonitorData));

        // Assert
        verify(parkingMonitorRepository, times(1)).saveAll(anyList());
    }

    private ParkingMonitorData getParkingMonitorData() {
        ParkingMonitorData parkingMonitorData = new ParkingMonitorData();
        parkingMonitorData.setLicensePlateNumber("TD-210-J");
        parkingMonitorData.setParkingStreet("Java");
        parkingMonitorData.setDatetimeOfObservation(LocalDateTime.now());
        return parkingMonitorData;
    }

    @Test
    public void testFindUnRegisteredPlates() {
        // Arrange
        ParkingMonitorDAO monitoredData = parkingMonitorService.mapFromDtoToDao(getParkingMonitorData());
        ParkingDetailsDAO parkingDetails = getParkingDetails("AA-111-A", "Java");
        when(parkingMonitorRepository.findAll()).thenReturn(Collections.singletonList(monitoredData));
        when(parkingDetailsRepository.findAll()).thenReturn(Collections.singletonList(parkingDetails));

        // Act
        List<ParkingMonitorDAO> unregisteredPlates = parkingMonitorService.findUnRegisteredPlates();

        // Assert
        assertEquals(Collections.singletonList(monitoredData), unregisteredPlates);
    }

    private ParkingDetailsDAO getParkingDetails(String license, String street) {
        return ParkingDetailsDAO.builder()
                .licensePlateNumber(license)
                .parkingStreet(street)
                .started(LocalDateTime.now().minusHours(2))
                .build();
    }

    @Test
    public void testIsPlateAndStreetUnregistered() {
        // Arrange
        ParkingMonitorDAO monitoredData = parkingMonitorService.mapFromDtoToDao(getParkingMonitorData());
        ParkingDetailsDAO matchingParkingDetails = getParkingDetails("AA-111-A", "Java");

        List<ParkingDetailsDAO> parkingDetailsList = Collections.singletonList(matchingParkingDetails);

        // Act & Assert
        assertTrue(parkingMonitorService.isPlateInStreetUnregistered(monitoredData, parkingDetailsList));
    }

    @Test
    public void testIsPlateAndStreetUnregistered_parkedInDifferentStreet() {
        // Arrange
        ParkingMonitorDAO monitoredData = parkingMonitorService.mapFromDtoToDao(getParkingMonitorData());
        ParkingDetailsDAO matchingParkingDetails = getParkingDetails("TD-210-J", "Jakarta");

        List<ParkingDetailsDAO> parkingDetailsList = Collections.singletonList(matchingParkingDetails);

        // Act & Assert
        assertTrue(parkingMonitorService.isPlateInStreetUnregistered(monitoredData, parkingDetailsList));
    }

    @Test
    public void testIsDatetimeMatching() {
        // Arrange
        LocalDateTime observationTime = LocalDateTime.now(ParkNLConstants.AMSTERDAM_ZONEID);
        ParkingDetailsDAO matchingParkingDetails = mock(ParkingDetailsDAO.class);
        ParkingDetailsDAO differentTimeParkingDetails = mock(ParkingDetailsDAO.class);

        when(matchingParkingDetails.getStarted()).thenReturn(observationTime.minusHours(1));
        when(matchingParkingDetails.getStopped()).thenReturn(observationTime.plusHours(1));

        when(differentTimeParkingDetails.getStarted()).thenReturn(observationTime.minusHours(3));
        when(differentTimeParkingDetails.getStopped()).thenReturn(observationTime.minusHours(2));

        // Act & Assert
        assertTrue(parkingMonitorService.isDatetimeMatching(observationTime, matchingParkingDetails));
        assertFalse(parkingMonitorService.isDatetimeMatching(observationTime, differentTimeParkingDetails));
    }
}