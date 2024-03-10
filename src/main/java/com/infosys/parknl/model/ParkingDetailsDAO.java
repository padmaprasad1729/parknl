package com.infosys.parknl.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "parking-details")
public class ParkingDetailsDAO {

    @Transient
    public static final String SEQUENCE_NAME = "parking_sequence";
    @Id
    private long id;

    @NotBlank
    @Size(max = 8)
    @Indexed(unique = true)
    private String licensePlateNumber;

    @NotBlank
    @Size(max = 40)
    private String parkingStreet;

    @NotBlank
    private LocalDateTime started;

    private LocalDateTime stopped;

    private BigDecimal cost;
}
