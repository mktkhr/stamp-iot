package com.example.stamp_app.controller.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "マイコン登録パラメータ")
public record MicroControllerPostParam(
        @Schema(description = "ユーザーID") @NotNull @Positive Long userId,
       @Schema(description = "MACアドレス") @NotBlank @Pattern(regexp = "^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$") String macAddress)
{
}
