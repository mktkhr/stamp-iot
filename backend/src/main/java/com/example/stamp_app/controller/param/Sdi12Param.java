package com.example.stamp_app.controller.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Sdi12Param {

    private Long sensorId;

    @NotNull
    private String sdiAddress;

    private String vwc;

    private String soilTemp;

    private String brp;

    private String sbec;

    private String spwec;

    private String gax;

    private String gay;

    private String gaz;

}
