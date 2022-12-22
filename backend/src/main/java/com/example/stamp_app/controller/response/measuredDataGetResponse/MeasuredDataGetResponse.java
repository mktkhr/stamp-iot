package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.EnvironmentalData;
import com.example.stamp_app.entity.MeasuredDataMaster;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MeasuredDataGetResponse {

    private List<Sdi12DataGetResponse> sdi12Data;

    private List<EnvironmentalDataGetResponse> environmentalData;

    private List<VoltageDataGetResponse> voltageData;

}
