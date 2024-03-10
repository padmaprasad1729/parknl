package com.infosys.parknl.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
public class ParkingMonitorData {

    @NotBlank
    @Size(max = 8)
    @Indexed(unique = true)
    private String licensePlateNumber;

    @NotBlank
    @Size(max = 40)
    private String parkingStreet;

    @NotBlank
    private LocalDateTime datetimeOfObservation;
}
