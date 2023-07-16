package com.example.stamp_app.controller.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "SDI-12パラメータ")
public class Sdi12Param {

    @Schema(description = "センサID", example = "1")
    private Long sensorId;

    @NotNull
    @Schema(description = "SDI-12アドレス", example = "1")
    private String sdiAddress;

    @Schema(description = "体積含水率", example = "11.11")
    private String vwc;

    @Schema(description = "地温", example = "11.11")
    private String soilTemp;

    @Schema(description = "比誘電率", example = "11.11")
    private String brp;

    @Schema(description = "電気伝導度", example = "11.11")
    private String sbec;

    @Schema(description = "間隙水電気伝導度", example = "11.11")
    private String spwec;

    @Schema(description = "重力加速度(X)", example = "1.11")
    private String gax;

    @Schema(description = "重力加速度(Y)", example = "1.11")
    private String gay;

    @Schema(description = "重力加速度(Z)", example = "1.11")
    private String gaz;

}
