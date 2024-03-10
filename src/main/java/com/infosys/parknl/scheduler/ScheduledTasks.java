package com.infosys.parknl.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.infosys.parknl.conf.ParkNLConstants;
import com.infosys.parknl.model.ParkingMonitorDAO;
import com.infosys.parknl.service.ParkingMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private final AtomicInteger count = new AtomicInteger(0);

	private final ParkingMonitorService parkingMonitorService;



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
	}

	private List<ParkingMonitorDAO> findUnRegisteredPlates() {
		return parkingMonitorService.findUnRegisteredPlates();
	}

	public int getInvocationCount() {
		return this.count.get();
	}
}