package com.infosys.parknl.service;

import java.time.*;

/**
 * ParkingTimeCalculator calculates parking time of a car.
 * Free parking daily between 21:00 - 08:00
 * and on Sundays for the whole day
 */
public class ParkingTimeCalculator {

    /***
     * Calculate time(in minutes) between two LocalDateTime with 2 conditions
     * - Free parking daily between 21:00 - 08:00
     * - and on Sundays for the whole day
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static long calculate(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        long minutes = 0;

        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        LocalTime ninePm = LocalTime.of(21, 0);
        LocalTime eightAm = LocalTime.of(8, 0);

        LocalTime zeroTime = LocalTime.of(0, 0);
        long numberOfDays = Duration.between(LocalDateTime.of(startDate, zeroTime), LocalDateTime.of(endDate, zeroTime)).toDays();

        // calculate minutes for each day
        for (int i = 0; i <= numberOfDays; i++) {

            startDate = startDate.plusDays(i);

            // START TIME CALCULATION

            // for non-first day, startTime is eightAm
            if (i != 0) {
                startTime = eightAm;
            }

            // skip sunday
            if (startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }

            // startTime less than 8 am, then startTime < 8am
            if (isLT(startTime, eightAm)) {
                startTime = eightAm;
            }

            // skip startTime > 9pm
            if (isGTE(startTime, ninePm)) {
                continue;
            }

            // END TIME CALCULATION

            // for last day endTime is the endDateTime's time
            if (i == numberOfDays) {
                endTime = endDateTime.toLocalTime();
            }

            // for last day
            if (startDate.isEqual(endDate)) {

                // skip endTime <= 8am
                if (isLTE(endTime, eightAm)) {
                    continue;
                }

                // endTime > 8am, then endTime is the endDateTime's time
                if (isGT(endTime, eightAm)) {
                    endTime = endDateTime.toLocalTime();
                }

                // endTime >= 9pm, then endTime is ninePm
                if (isGTE(endTime, ninePm)) {
                    endTime = ninePm;
                }
            } else {
                // for non-last day endTime is ninePm
                endTime = ninePm;
            }

            minutes += Duration.between(startTime, endTime).toMinutes();
        }
        return minutes;
    }

    private static boolean isLT(LocalTime startTime, LocalTime eightAm) {
        return startTime.isBefore(eightAm);
    }

    private static boolean isGT(LocalTime endTime, LocalTime eightAm) {
        return endTime.isAfter(eightAm);
    }

    private static boolean isLTE(LocalTime endTime, LocalTime eightAm) {
        return endTime.isBefore(eightAm) || endTime.equals(eightAm);
    }

    private static boolean isGTE(LocalTime startTime, LocalTime ninePm) {
        return !startTime.isBefore(ninePm);
    }
}
