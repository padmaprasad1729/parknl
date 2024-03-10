package com.infosys.parknl.controller;

import com.infosys.parknl.model.ParkingMonitorData;
import com.infosys.parknl.service.ParkingMonitorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingMonitorControllerTest {

    @Mock
    private ParkingMonitorService parkingMonitorService;

    @InjectMocks
    private ParkingMonitorController parkingMonitorController;

    @Test
    public void testAddMonitoredData() throws Exception {
        // Arrange
        ParkingMonitorData parkingMonitorData = new ParkingMonitorData();
        parkingMonitorData.setLicensePlateNumber("TD-210-J");
        parkingMonitorData.setParkingStreet("Java");
        parkingMonitorData.setDatetimeOfObservation(LocalDateTime.now());

        List<ParkingMonitorData> list = List.of(parkingMonitorData);
        doNothing().when(parkingMonitorService).addParkingMonitorData(list);

        // Act
        parkingMonitorController.addMonitoredData(list);

        //Assert
        verify(parkingMonitorService, times(1)).addParkingMonitorData(Collections.singletonList(parkingMonitorData));
    }
}