package com.example.stamp_app.controller.response.measuredDataGetResponse;

import lombok.Data;

import java.util.List;

@Data
public class MeasuredDataGetResponse {

    private List<Sdi12DataGetResponse> sdi12Data;

    private List<EnvironmentalDataGetResponse> environmentalData;

    private List<VoltageDataGetResponse> voltageData;

}
