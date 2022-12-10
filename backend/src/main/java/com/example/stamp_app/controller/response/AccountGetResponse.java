package com.example.stamp_app.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AccountGetResponse {

    private BigInteger id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
