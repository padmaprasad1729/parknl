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

    @InjectMocks
    private ParkingServiceImpl parkingService;

    @Test
    public void testRegister() {
        // Arrange
        RegisterDTO registerDTO = getRegisterDTO();
        when(seqService.generateSequence(any())).thenReturn(1L);
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(Collections.emptyList());
        when(streetService.getStreetOrFailOnEmpty("Java")).thenReturn(getStreetDetailsDAO());
        // Act
        String result = parkingService.register(registerDTO);

        // Assert
        assertNotNull(result);
        verify(repo, times(1)).save(any());
    }


    @Test
    public void testRegister_parkedSomewhereElse() {
        // Arrange
        RegisterDTO registerDTO = getRegisterDTO();
        ParkingDetailsDAO parkingDetailsDAO = ParkingDetailsDAO.builder()
                .parkingStreet("Java")
                .build();
        parkingDetailsDAO.setStarted(LocalDateTime.now().minusHours(1));
        when(repo.findByLicensePlateNumber("TD-210-J")).thenReturn(Collections.singletonList(parkingDetailsDAO));
        when(streetService.getStreetOrFailOnEmpty("Java")).thenReturn(getStreetDetailsDAO());

        // Act & Assert
        assertThrows(RegistrationNotPossibleException.class, () -> parkingService.register(registerDTO));
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
    public void testUnregister_withZeroCost() {
        // Arrange
        UnRegisterDTO unRegisterDTO = getUnRegisterDTO();
        ParkingDetailsDAO parkingDetailsDAO = ParkingDetailsDAO.builder()
                .parkingStreet("Java")
                .build();
        parkingDetailsDAO.setStarted(LocalDateTime.now());
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

}