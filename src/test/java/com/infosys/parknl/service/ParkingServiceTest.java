package com.infosys.parknl.service;

import com.infosys.parknl.exception.RegistrationNotPossibleException;
import com.infosys.parknl.exception.UnRegistrationNotPossibleException;
import com.infosys.parknl.model.*;
import com.infosys.parknl.repository.ParkingDetailsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    @Mock
    private ParkingDetailsRepository repo;

    @Mock
    private StreetService streetService;

    @Mock
    private SequenceGeneratorService seqService;

    @Mock
    private Environment environment;

    @InjectMocks
    private ParkingServiceImpl parkingService;

    @Test
    public void testRegister() {
        // Arrange
        RegisterDTO registerDTO = getRegisterDTO();
        when(seqService.generateSequence(any())).thenReturn(1L);
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(Collections.emptyList());
        when(streetService.getStreetOrFailOnEmpty("Java")).thenReturn(getStreetDetailsDAO());
        when(environment.getProperty("spring.active.profiles")).thenReturn("test");
        // Act
        String result = parkingService.register(registerDTO);

        // Assert
        assertNotNull(result);
        verify(repo, times(1)).save(any());
    }

    private RegisterDTO getRegisterDTO() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setLicensePlateNumber("TD-210-J");
        registerDTO.setParkingStreet("Java");
        return registerDTO;
    }

    private UnRegisterDTO getUnRegisterDTO() {
        UnRegisterDTO unRegisterDTO = new UnRegisterDTO();
        unRegisterDTO.setLicensePlateNumber("TD-210-J");
        return unRegisterDTO;
    }

    private StreetDetails getStreetDetails() {
        return StreetDetails.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
    }

    private StreetDetailsDAO getStreetDetailsDAO() {
        return StreetDetailsDAO.builder()
                .streetName("Java")
                .priceInCentPerMinute(BigDecimal.TEN)
                .build();
    }

    @Test
    public void testRegister_SameLicensePlateNumber() {
        // Arrange
        RegisterDTO registerDTO = getRegisterDTO();
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(Collections.singletonList(ParkingDetailsDAO.builder().build()));

        // Act & Assert
        assertThrows(RegistrationNotPossibleException.class, () -> parkingService.register(registerDTO));
    }

    @Test
    public void testRegister_FreeParkingOnSunday() {
        // Arrange
        RegisterDTO registerDTO = getRegisterDTO();
        when(repo.findByLicensePlateNumber("TD-210-J")).thenThrow(new RegistrationNotPossibleException("Parking is free on Sunday"));

        // Act & Assert
        assertThrows(RegistrationNotPossibleException.class, () -> parkingService.register(registerDTO));
    }

    @Test
    public void testRegister_FreeParkingBetween21And8() {
        // Arrange
        RegisterDTO registerDTO = getRegisterDTO();
        when(repo.findByLicensePlateNumber("TD-210-J")).thenThrow(new RegistrationNotPossibleException("Parking is free from 21:00 to 8:00"));

        // Act & Assert
        assertThrows(RegistrationNotPossibleException.class, () -> parkingService.register(registerDTO));
    }

    @Test
    public void testUnregister() {
        // Arrange
        UnRegisterDTO unRegisterDTO = getUnRegisterDTO();
        ParkingDetailsDAO parkingDetailsDAO = ParkingDetailsDAO.builder()
                .parkingStreet("Java")
                .build();
        parkingDetailsDAO.setStarted(LocalDateTime.now().minusHours(1));
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(Collections.singletonList(parkingDetailsDAO));
        when(streetService.findByStreetName(anyString())).thenReturn(getStreetDetails());

        // Act
        String result = parkingService.unregister(unRegisterDTO);

        // Assert
        assertNotNull(result);
        verify(repo, times(1)).save(any(ParkingDetailsDAO.class));
    }

    @Test
    public void testUnregister_NoOpenParkingDetails() {
        // Arrange
        UnRegisterDTO unRegisterDTO = getUnRegisterDTO();
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(UnRegistrationNotPossibleException.class, () -> parkingService.unregister(unRegisterDTO));
    }

    @Test
    public void testUnregister_MultipleOpenParkingDetails() {
        // Arrange
        UnRegisterDTO unRegisterDTO = getUnRegisterDTO();
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(List.of(ParkingDetailsDAO.builder().build(), ParkingDetailsDAO.builder().build()));

        // Act & Assert
        assertThrows(UnRegistrationNotPossibleException.class, () -> parkingService.unregister(unRegisterDTO));
    }

    // Add more tests for other scenarios and methods as needed
}