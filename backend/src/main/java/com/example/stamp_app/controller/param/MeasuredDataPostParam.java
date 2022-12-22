package com.example.stamp_app.controller.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class MeasuredDataPostParam {

    @NotNull
    private String macAddress;

    private List<Sdi12Param> sdi12Param;

    private List<EnvironmentalDataParam> environmentalDataParam;

    private String voltage;
}