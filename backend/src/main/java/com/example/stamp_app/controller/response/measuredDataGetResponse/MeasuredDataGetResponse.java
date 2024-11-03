package com.example.stamp_app.controller.response.measuredDataGetResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Schema(description = "測定結果取得レスポンス")
public class MeasuredDataGetResponse {

    @Schema(description = "SDI-12データリスト")
    private List<Sdi12DataGetResponse> sdi12Data;

    @Schema(description = "環境データリスト")
    private List<EnvironmentalDataGetResponse> environmentalData;

    @Schema(description = "電圧データ")
    private List<VoltageDataGetResponse> voltageData;

}
