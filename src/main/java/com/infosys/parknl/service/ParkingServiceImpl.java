package com.infosys.parknl.service;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.exception.RegistrationNotPossibleException;
import com.infosys.parknl.exception.UnRegistrationNotPossibleException;
import com.infosys.parknl.model.ParkingDetailsDAO;
import com.infosys.parknl.model.RegisterDTO;
import com.infosys.parknl.model.StreetDetails;
import com.infosys.parknl.model.UnRegisterDTO;
import com.infosys.parknl.repository.ParkingDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ParkingDetailsRepository repo;

    private final StreetService streetService;

    private final SequenceGeneratorService seqService;

    public ParkingServiceImpl(ParkingDetailsRepository repo, StreetService streetService, SequenceGeneratorService seqService) {
        this.repo = repo;
        this.streetService = streetService;
        this.seqService = seqService;
    }

    public String register(RegisterDTO input) {
        log.info("Register with input: {}", input);

        validateStreet(input.getParkingStreet());

        List<ParkingDetailsDAO> parkingDetailsDAOS = repo.findByLicensePlateNumber(input.getLicensePlateNumber());
        if (!getOpenParkingDetails(parkingDetailsDAOS).isEmpty()) {
            throw new RegistrationNotPossibleException("Vehicle with same license plate number is parked somewhere else");
        }

        var dateTime = LocalDateTime.now(ParkNLConstants.AMSTERDAM_ZONEID);

        ParkingDetailsDAO parkingDetailsDAO = ParkingDetailsDAO.builder()
                .id(seqService.generateSequence(ParkingDetailsDAO.SEQUENCE_NAME))
                .licensePlateNumber(input.getLicensePlateNumber())
                .parkingStreet(input.getParkingStreet())
                .started(dateTime)
                .build();
        repo.save(parkingDetailsDAO);

        log.info("Registered with input: {}, response: {}", input, parkingDetailsDAO);

        return "{\"message\": \"PARKING HAS STARTED AT " + dateTime + " \"}";
    }

    private void validateStreet(String parkingStreet) {
        streetService.getStreetOrFailOnEmpty(parkingStreet);
    }

    private List<ParkingDetailsDAO> getOpenParkingDetails(List<ParkingDetailsDAO> parkingDetailsDAOS) {
        return parkingDetailsDAOS.stream().filter(p -> p.getStopped() == null).collect(Collectors.toList());
    }

    public String unregister(UnRegisterDTO input) {
        log.info("UnRegister with input: {}", input);

        List<ParkingDetailsDAO> list = repo.findByLicensePlateNumber(input.getLicensePlateNumber());
        List<ParkingDetailsDAO> parkingDetailsDAOS = getOpenParkingDetails(list);

        if (parkingDetailsDAOS.isEmpty()) {
            throw new UnRegistrationNotPossibleException("Vehicle with this license plate number is not parked anywhere");
        } else if (parkingDetailsDAOS.size() > 1) {
            throw new UnRegistrationNotPossibleException("Vehicle with this license plate number is parked in multiple places");
        } else {
            var dateTime = LocalDateTime.now(ParkNLConstants.AMSTERDAM_ZONEID);
            ParkingDetailsDAO parkingDetailsDAO = parkingDetailsDAOS.get(0);
            parkingDetailsDAO.setStopped(dateTime);
            parkingDetailsDAO.setCost(calculateCost(parkingDetailsDAO));
            repo.save(parkingDetailsDAO);

            log.info("UnRegistered with input: {}, response: {}", input, parkingDetailsDAO);

            return "{\"message\": \"PARKING SESSION HAS ENDED AT " + dateTime + " \"}";
        }
    }

    private BigDecimal calculateCost(ParkingDetailsDAO parkingDetailsDAO) {

        long numberOfMinutes = ParkingTimeCalculator.calculate(parkingDetailsDAO.getStarted(), parkingDetailsDAO.getStopped());

        StreetDetails streetDetails = streetService.findByStreetName(parkingDetailsDAO.getParkingStreet());
        BigDecimal euros = centToEuro(streetDetails.getPriceInCentPerMinute().multiply(BigDecimal.valueOf(numberOfMinutes)));
        log.debug("parking details: {}, numberOfMinutes: {}, euros: {}", parkingDetailsDAO, numberOfMinutes, euros);

        return euros;
    }

    private BigDecimal centToEuro(BigDecimal cent) {
        if (Objects.equals(cent, BigDecimal.ZERO)) return BigDecimal.ZERO;
        return cent.divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
    }
}
