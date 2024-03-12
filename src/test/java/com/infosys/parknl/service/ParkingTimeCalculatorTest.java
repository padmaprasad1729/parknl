package com.infosys.parknl.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
public class ParkingTimeCalculatorTest {

    @Test
    public void calculateFor_Sunday_Free() {

        LocalDate localStartDate = LocalDate.of(2024, 3, 10); // Sunday
        LocalTime localStartTime = LocalTime.of(8, 0); // 8 AM

        LocalDate localEndDate = LocalDate.of(2024, 3, 10); // Sunday
        LocalTime localEndTime= LocalTime.of(22, 0); // 10 PM

        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);

        long minutes = ParkingTimeCalculator.calculate(startDateTime, endDateTime);

        Assertions.assertEquals(minutes, 0); // Sunday Free
    }

    @Test
    public void calculateFor_Time21And08_Free() {

        LocalDate localStartDate = LocalDate.of(2024, 3, 11); // Monday
        LocalTime localStartTime = LocalTime.of(21, 0); // 9 PM

        LocalDate localEndDate = LocalDate.of(2024, 3, 12); // Tuesday
        LocalTime localEndTime= LocalTime.of(8, 0); // 8 AM

        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);

        long minutes = ParkingTimeCalculator.calculate(startDateTime, endDateTime);

        Assertions.assertEquals(minutes, 0); // Time21And08_Free
    }

    @Test
    public void calculateFor_Time20And08_60MinutesFrom20To21() {

        LocalDate localStartDate = LocalDate.of(2024, 3, 11); // Monday
        LocalTime localStartTime = LocalTime.of(20, 0); // 8 PM

        LocalDate localEndDate = LocalDate.of(2024, 3, 12); // Tuesday
        LocalTime localEndTime= LocalTime.of(8, 0); // 8 AM

        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);

        long minutes = ParkingTimeCalculator.calculate(startDateTime, endDateTime);

        Assertions.assertEquals(minutes, 60); // Time20And08_60MinutesFrom20To21
    }

    @Test
    public void calculateFor_Time21And09_60MinutesFrom08To09() {

        LocalDate localStartDate = LocalDate.of(2024, 3, 11); // Monday
        LocalTime localStartTime = LocalTime.of(21, 0); // 9 PM

        LocalDate localEndDate = LocalDate.of(2024, 3, 12); // Tuesday
        LocalTime localEndTime= LocalTime.of(9, 0); // 9 AM

        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);

        long minutes = ParkingTimeCalculator.calculate(startDateTime, endDateTime);

        Assertions.assertEquals(minutes, 60); // Time21And09_60MinutesFrom08To09
    }

    @Test
    public void calculateFor_Time20_59And08_01_2Minutes() {

        LocalDate localStartDate = LocalDate.of(2024, 3, 11); // Monday
        LocalTime localStartTime = LocalTime.of(20, 59); // 8.59 PM

        LocalDate localEndDate = LocalDate.of(2024, 3, 12); // Tuesday
        LocalTime localEndTime= LocalTime.of(8, 1); // 8.01 AM

        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);

        long minutes = ParkingTimeCalculator.calculate(startDateTime, endDateTime);

        Assertions.assertEquals(minutes, 2); // Time20_59And08_01_2Minutes
    }

    @Test
    public void calculateFor_Time07_59And21_01_780Minutes() {

        LocalDate localStartDate = LocalDate.of(2024, 3, 11); // Monday
        LocalTime localStartTime = LocalTime.of(7, 59); // 7.59 AM

        LocalDate localEndDate = LocalDate.of(2024, 3, 11); // Monday
        LocalTime localEndTime= LocalTime.of(21, 1); // 9.01 PM

        LocalDateTime startDateTime = LocalDateTime.of(localStartDate, localStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(localEndDate, localEndTime);

        long minutes = ParkingTimeCalculator.calculate(startDateTime, endDateTime);

        Assertions.assertEquals(minutes, 13 * 60); // Time07_59And21_01_780Minutes
    }
}
