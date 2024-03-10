package com.infosys.parknl.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnRegisterDTO {

    @NotBlank
    @LicensePlateFormatValidator
    private String licensePlateNumber;
}
