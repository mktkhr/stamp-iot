package com.example.stamp_app.controller.param;

import com.example.stamp_app.controller.response.measuredDataGetResponse.EnvironmentalDataGetResponse;
import com.example.stamp_app.controller.response.measuredDataGetResponse.Sdi12DataGetResponse;
import com.example.stamp_app.controller.response.measuredDataGetResponse.VoltageDataGetResponse;
import lombok.Data;

import java.util.List;

@Data
public class MeasuredDataGetParam {

    private List<Sdi12DataGetResponse> sdi12DataList;

    private List<EnvironmentalDataGetResponse> environmentalDataList;

    private List<VoltageDataGetResponse> voltageDataList;

}
