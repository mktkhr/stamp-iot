package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.EnvironmentalData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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

    public static EnvironmentalDataGetResponse convertFromEnvironmentalData(
            @NotNull final EnvironmentalData environmentalData,
            @NotNull final Long measuredDataMasterId,
            @NotNull final String dayOfYear
    ) {

        return new EnvironmentalDataGetResponse(
                measuredDataMasterId,
                dayOfYear,
                environmentalData.getAirPress(),
                environmentalData.getTemp(),
                environmentalData.getHumi(),
                environmentalData.getCo2Concent(),
                environmentalData.getTvoc(),
                environmentalData.getAnalogValue(),
                environmentalData.getMeasuredDataMaster().getCreatedAt(),
                environmentalData.getMeasuredDataMaster().getUpdatedAt(),
                environmentalData.getMeasuredDataMaster().getDeletedAt()
        );

    }

}
