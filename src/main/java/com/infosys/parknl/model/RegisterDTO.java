package com.infosys.parknl.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank
    @LicensePlateFormatValidator
    private String licensePlateNumber;

    @NotBlank
    private String parkingStreet;
}
