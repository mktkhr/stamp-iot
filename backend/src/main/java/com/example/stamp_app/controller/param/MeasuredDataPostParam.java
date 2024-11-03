package com.example.stamp_app.controller.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@Schema(description = "測定結果登録パラメータ")
public class MeasuredDataPostParam {

    @NotBlank
    @Schema(description = "MACアドレス", example = "AA:AA:AA:AA:AA:AA")
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$")
    private String macAddress;

    @Schema(description = "SDI-12パラメータリスト")
    private List<Sdi12Param> sdi12Param;

    @Schema(description = "環境データパラメータリスト")
    private List<EnvironmentalDataParam> environmentalDataParam;

    @Schema(description = "電圧", example = "11.11")
    private String voltage;
}