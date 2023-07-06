package com.example.stamp_app.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigInteger;

@Data
public class MicroControllerPostParam {

    @NotNull
    @Positive
    private BigInteger userId;

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$")
    private String macAddress;

}
