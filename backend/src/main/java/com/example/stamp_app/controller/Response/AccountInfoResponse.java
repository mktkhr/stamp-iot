package com.example.stamp_app.controller.Response;

import lombok.Data;

import java.math.BigInteger;

@Data
public class AccountInfoResponse {
    private BigInteger id;
    private String name;
}
