package com.example.stamp_app.controller.response;

import com.example.stamp_app.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

public record AccountLoginResponse(
        HttpStatus status,
        Account account
) {
}
