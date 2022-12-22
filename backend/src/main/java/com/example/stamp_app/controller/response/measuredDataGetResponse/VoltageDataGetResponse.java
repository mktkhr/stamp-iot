package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.EnvironmentalData;
import com.example.stamp_app.entity.MeasuredDataMaster;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class VoltageDataGetResponse {

    private BigInteger measuredDataMasterId;

    private String dayOfYear;

    private String voltage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public static VoltageDataGetResponse convertFromMeasuredDataMaster(MeasuredDataMaster measuredDataMaster) {

        var voltageDataGetResponse = new VoltageDataGetResponse();
        voltageDataGetResponse.setMeasuredDataMasterId(measuredDataMaster.getId());
        voltageDataGetResponse.setDayOfYear(measuredDataMaster.getDayOfYear());
        voltageDataGetResponse.setVoltage(measuredDataMaster.getVoltage());
        voltageDataGetResponse.setCreatedAt(measuredDataMaster.getCreatedAt());
        voltageDataGetResponse.setUpdatedAt(measuredDataMaster.getUpdatedAt());
        voltageDataGetResponse.setDeletedAt(measuredDataMaster.getDeletedAt());

        return voltageDataGetResponse;
    }

}
