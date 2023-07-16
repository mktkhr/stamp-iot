package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.EnvironmentalData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "環境データ")
public class EnvironmentalDataGetResponse {

    @Schema(description = "測定データマスターID", example = "1")
    private Long measuredDataMasterId;

    @Schema(description = "DOY", example = "111.11")
    private String dayOfYear;

    @Schema(description = "大気圧", example = "1011.11")
    private String airPress;

    @Schema(description = "気温", example = "11.11")
    private String temp;

    @Schema(description = "相対湿度", example = "77.77")
    private String humi;

    @Schema(description = "二酸化炭素濃度", example = "1111.11")
    private String co2Concent;

    @Schema(description = "総揮発性有機化合物", example = "11.11")
    private String tvoc;

    @Schema(description = "アナログ値", example = "11.11")
    private String analogValue;

    @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime updatedAt;

    @Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime deletedAt;

    public static EnvironmentalDataGetResponse convertFromEnvironmentalData(EnvironmentalData environmentalData, Long measuredDataMasterId, String dayOfYear) {
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
