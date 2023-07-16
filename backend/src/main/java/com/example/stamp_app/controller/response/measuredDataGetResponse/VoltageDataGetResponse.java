package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.MeasuredDataMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoltageDataGetResponse {

    @Schema(description = "測定データマスターID", example = "1")
    private Long measuredDataMasterId;

    @Schema(description = "DOY", example = "111.11")
    private String dayOfYear;

    @Schema(description = "電圧", example = "11.11")
    private String voltage;

    @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime updatedAt;

    @Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
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
