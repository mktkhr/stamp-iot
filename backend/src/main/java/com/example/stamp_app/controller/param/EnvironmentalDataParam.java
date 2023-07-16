package com.example.stamp_app.controller.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "環境データパラメータ")
public class EnvironmentalDataParam {

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

}
