package com.example.stamp_app.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class MeasuredDataPostParam {

    @NotBlank
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$")
    private String macAddress;

    private List<Sdi12Param> sdi12Param;

    private List<EnvironmentalDataParam> environmentalDataParam;

    private String voltage;
}