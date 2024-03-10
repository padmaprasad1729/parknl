package com.infosys.parknl.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StreetDetails {

    @NotBlank
    private String streetName;

    @NotBlank
    private BigDecimal priceInCentPerMinute;
}
