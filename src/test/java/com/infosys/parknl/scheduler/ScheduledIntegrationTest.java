//package com.infosys.parknl.scheduler;
//
//import com.infosys.parknl.conf.ScheduledConfig;
//import com.infosys.parknl.service.ParkingMonitorService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringJUnitConfig(ScheduledConfig.class)
//public class ScheduledIntegrationTest {
//
//    @Autowired
//    private ScheduledTasks scheduledTasks;
//
//    @Autowired
//    private ParkingMonitorService parkingMonitorService;
//
//    @Test
//    public void createReportForUnRegisteredPlates_test() throws InterruptedException {
//        Thread.sleep(60 * 1000L);
//
//        assertThat(scheduledTasks.getInvocationCount()).isGreaterThan(0);
//    }
//}