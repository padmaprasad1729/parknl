package com.infosys.parknl.scheduler;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.model.ParkingMonitorDAO;
import com.infosys.parknl.service.ParkingMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final AtomicInteger count = new AtomicInteger(0);

    private final ParkingMonitorService parkingMonitorService;

    @Value("${unregistered-plates-report}")
    String filename;

    public ScheduledTasks(ParkingMonitorService parkingMonitorService) {
        this.parkingMonitorService = parkingMonitorService;
    }

    @Scheduled(cron = "${cron.expression:10 * * * * ?}", zone = ParkNLConstants.AMSTERDAM_ZONE)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "${cron.expression:10 * * * * ?}", zone = ParkNLConstants.AMSTERDAM_ZONE)
    public void createReportForUnRegisteredPlates() {

        this.count.incrementAndGet();

        log.info("Creating report for un-registered plates at {}", dateFormat.format(new Date()));
        List<ParkingMonitorDAO> dataList = findUnRegisteredPlates();
        log.info("UnRegistered Plates count: {}", dataList.size());
        log.debug("UnRegistered Plates dataList: {}", dataList);

        if (!dataList.isEmpty()) {
            writeToFile(dataList);
        }
    }

    private void writeToFile(List<ParkingMonitorDAO> dataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,LicensePlateNumber,parkingStreet,datetimeOfObservation \n");
        for (ParkingMonitorDAO daoData : dataList) {
            sb.append(daoData.getId());
            sb.append(",");
            sb.append(daoData.getLicensePlateNumber());
            sb.append(",");
            sb.append(daoData.getParkingStreet());
            sb.append(",");
            sb.append(daoData.getDatetimeOfObservation());
            sb.append("\n");
        }
        String datetime = LocalDateTime.now(ParkNLConstants.AMSTERDAM_ZONEID).toString();
        Path path = Paths.get(filename.replace("date", datetime));
        try {
            Files.writeString(path, sb.toString(), StandardOpenOption.CREATE_NEW);
            clearMonitoredDataDB();
            clearParkingDetailsDB();
        } catch (IOException e) {
            log.error("Failed to createReportForUnRegisteredPlates at " + datetime);
        }
    }

    private void clearMonitoredDataDB() {
        parkingMonitorService.clearMonitoredDataDB();
    }

    private void clearParkingDetailsDB() {
        parkingMonitorService.clearParkingDetailsDB();
    }

    private List<ParkingMonitorDAO> findUnRegisteredPlates() {
        return parkingMonitorService.findUnRegisteredPlates();
    }

    public int getInvocationCount() {
        return this.count.get();
    }
}