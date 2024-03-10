package com.infosys.parknl.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@Document(collection = "street-details")
public class StreetDetailsDAO {

    @Transient
    public static final String SEQUENCE_NAME = "street_sequence";
    @Id
    private long id;
    private String streetName;

    private BigDecimal priceInCentPerMinute;
}
