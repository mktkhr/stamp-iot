package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.EnvironmentalData;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class EnvironmentalDataGetResponse {

    private BigInteger measuredDataMasterId;

    private String dayOfYear;

    private String airPress;

    private String temp;

    private String humi;

    private String co2Concent;

    private String tvoc;

    private String analogValue;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static EnvironmentalDataGetResponse convertFromEnvironmentalData(EnvironmentalData environmentalData, BigInteger measuredDataMasterId, String dayOfYear) {
        var environmentalDataGetResponse = new EnvironmentalDataGetResponse();
        environmentalDataGetResponse.setMeasuredDataMasterId(measuredDataMasterId);
        environmentalDataGetResponse.setDayOfYear(dayOfYear);
        environmentalDataGetResponse.setAirPress(environmentalData.getAirPress());
        environmentalDataGetResponse.setTemp(environmentalData.getTemp());
        environmentalDataGetResponse.setHumi(environmentalData.getHumi());
        environmentalDataGetResponse.setCo2Concent(environmentalData.getCo2Concent());
        environmentalDataGetResponse.setTvoc(environmentalData.getTvoc());
        environmentalDataGetResponse.setAnalogValue(environmentalData.getAnalogValue());
        environmentalDataGetResponse.setCreatedAt(environmentalData.getMeasuredDataMaster().getCreatedAt());
        environmentalDataGetResponse.setUpdatedAt(environmentalData.getMeasuredDataMaster().getUpdatedAt());
        environmentalDataGetResponse.setDeletedAt(environmentalData.getMeasuredDataMaster().getDeletedAt());
        return environmentalDataGetResponse;
    }

}
