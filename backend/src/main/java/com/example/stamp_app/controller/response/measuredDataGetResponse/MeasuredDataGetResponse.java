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

//    private BigInteger id;
//
//    private String dayOfYear;
//
//    private LocalDateTime createdAt;
//
//    private LocalDateTime updatedAt;
//
//    private LocalDateTime deletedAt;

    private List<Sdi12DataGetResponse> sdi12Data;

    private List<EnvironmentalDataGetResponse> environmentalData;

    private List<VoltageDataGetResponse> voltageData;

//    public static List<MeasuredDataGetResponse> convertMeasuredDataResponse(List<MeasuredDataMaster> measuredDataMasterList) {
//        var measuredDataResponseList = new ArrayList<MeasuredDataGetResponse>();
//
//        measuredDataMasterList.forEach((measuredDataMaster) -> {
//            MeasuredDataGetResponse measuredDataGetResponse = new MeasuredDataGetResponse();
//
//            measuredDataGetResponse.setId(measuredDataMaster.getId());
//            measuredDataGetResponse.setDayOfYear(measuredDataMaster.getDayOfYear());
//            measuredDataGetResponse.setCreatedAt(measuredDataMaster.getCreatedAt());
//            measuredDataGetResponse.setUpdatedAt(measuredDataMaster.getUpdatedAt());
//            measuredDataGetResponse.setDeletedAt(measuredDataMaster.getDeletedAt());
//            measuredDataGetResponse.setSdi12Data(measuredDataMaster.getSdi12Data());
//            measuredDataGetResponse.setEnvironmentalData(measuredDataMaster.getEnvironmentalData());
//            measuredDataGetResponse.setVoltage(measuredDataMaster.getVoltage());
//
//            measuredDataResponseList.add(measuredDataGetResponse);
//        });
//
//        return measuredDataResponseList;
//    }

}
