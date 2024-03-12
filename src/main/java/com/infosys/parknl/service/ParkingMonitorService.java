package com.infosys.parknl.service;

import com.infosys.parknl.model.ParkingDetailsDAO;
import com.infosys.parknl.model.ParkingMonitorDAO;
import com.infosys.parknl.model.ParkingMonitorData;
import com.infosys.parknl.repository.ParkingDetailsRepository;
import com.infosys.parknl.repository.ParkingMonitorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingMonitorService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ParkingMonitorRepository parkingMonitorRepository;

    private final ParkingDetailsRepository parkingDetailsRepository;

    private final SequenceGeneratorService seqService;

    public ParkingMonitorService(ParkingMonitorRepository parkingMonitorRepository,
                                 ParkingDetailsRepository parkingDetailsRepository,
                                 SequenceGeneratorService seqService) {
        this.parkingMonitorRepository = parkingMonitorRepository;
        this.parkingDetailsRepository = parkingDetailsRepository;
        this.seqService = seqService;
    }

    public void addParkingMonitorData(List<ParkingMonitorData> parkingMonitorDataList) {
        List<ParkingMonitorDAO> dataList = parkingMonitorDataList.stream()
                .map(this::mapFromDtoToDao)
                .collect(Collectors.toList());
        this.parkingMonitorRepository.saveAll(dataList);

    }

    ParkingMonitorDAO mapFromDtoToDao(ParkingMonitorData p) {
        return ParkingMonitorDAO.builder()
                .id(this.seqService.generateSequence(ParkingMonitorDAO.SEQUENCE_NAME))
                .licensePlateNumber(p.getLicensePlateNumber())
                .parkingStreet(p.getParkingStreet())
                .datetimeOfObservation(p.getDatetimeOfObservation())
                .build();
    }

    public List<ParkingMonitorDAO> findUnRegisteredPlates() {
        List<ParkingMonitorDAO> monitoredDataList = parkingMonitorRepository.findAll();
        List<ParkingDetailsDAO> parkingDetailsList = parkingDetailsRepository.findAll();

        return monitoredDataList.stream()
                .filter(monitoredData -> isPlateInStreetUnregistered(monitoredData, parkingDetailsList))
                .collect(Collectors.toList());
    }

    boolean isPlateInStreetUnregistered(ParkingMonitorDAO monitoredData, List<ParkingDetailsDAO> parkingDetailsList) {
        return parkingDetailsList.stream()
                .noneMatch(p -> (monitoredData.getLicensePlateNumber().equals(p.getLicensePlateNumber())
                        && monitoredData.getParkingStreet().equals(p.getParkingStreet()))
                        && isDatetimeMatching(monitoredData.getDatetimeOfObservation(), p));
    }

    boolean isDatetimeMatching(LocalDateTime datetimeOfObservation, ParkingDetailsDAO parkingDetails) {
        return (datetimeOfObservation.isAfter(parkingDetails.getStarted()) || datetimeOfObservation.isEqual(parkingDetails.getStarted()))
                && (datetimeOfObservation.isEqual(parkingDetails.getStopped()) || datetimeOfObservation.isBefore(parkingDetails.getStopped()));
    }

    public void clearMonitoredDataDB() {
        parkingMonitorRepository.deleteAll();
    }

    public void clearParkingDetailsDB() {
        parkingDetailsRepository.deleteWhenCostIsGreaterThanZero();
    }
}
