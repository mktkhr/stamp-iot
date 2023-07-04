package com.example.stamp_app.controller.param.microController;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;


@Getter
@AllArgsConstructor
@Validated
public class MicroControllerPatchParam {

    @Pattern(regexp = "^([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})$")
    @NotNull
    private String microControllerUuid;

    private String name;

    @Pattern(regexp = "^(60|30|20|15|10|5|1)$")
    @NotNull
    private String interval;

    @Pattern(regexp = "^(([0-9A-Za-z]{1},)*[0-9A-za-z]{1})|([0-9A-za-z]{1})$")
    @Nullable
    private String sdi12Address;
}
