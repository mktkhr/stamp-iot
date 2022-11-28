package com.example.stamp_app.controller.Param;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Sdi12Param {

    private String sensorId;

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
