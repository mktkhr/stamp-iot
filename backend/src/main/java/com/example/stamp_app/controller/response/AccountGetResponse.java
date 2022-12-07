package com.example.stamp_app.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class AccountGetResponse {

    private HttpStatus status;

    private BigInteger id;

    private String name;

}
