package com.example.stamp_app.controller.param.microController;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;


@Getter
@Schema(description = "マイコン更新データ")
@AllArgsConstructor
@Validated
public class MicroControllerPatchParam {

    @NotNull
    @Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
    @Pattern(regexp = "^([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})$")
    private String microControllerUuid;

    @Schema(description = "マイコン名", example = "サンプル端末")
    private String name;

    @NotNull
    @Schema(description = "測定間隔", example = "60")
    @Pattern(regexp = "^(60|30|20|15|10|5|1)$")
    private String interval;

    @Nullable
    @Schema(description = "SDI-12アドレス", example = "1,3")
    @Pattern(regexp = "^((([0-9A-Za-z]{1},)+[0-9A-za-z]{1})|([0-9A-za-z]{1})?)$")
    private String sdi12Address;
}
