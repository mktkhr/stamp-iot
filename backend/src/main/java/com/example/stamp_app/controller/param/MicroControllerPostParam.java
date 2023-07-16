package com.example.stamp_app.controller.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "マイコン登録パラメータ")
public class MicroControllerPostParam {

    @NotNull
    @Positive
    @Schema(description = "ユーザーID")
    private Long userId;

    @NotBlank
    @Schema(description = "MACアドレス")
    @Pattern(regexp = "^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$")
    private String macAddress;

}
