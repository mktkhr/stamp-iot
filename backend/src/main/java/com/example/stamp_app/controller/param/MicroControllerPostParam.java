package com.example.stamp_app.controller.param;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigInteger;

@Data
public class MicroControllerPostParam {

    @NotNull
    private BigInteger userId;

    @NotNull
    @Pattern(regexp = "^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$")
    private String macAddress;

}
