package com.example.stamp_app.controller.Response;

import com.example.stamp_app.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class AccountLoginResponse {

    private HttpStatus status;

    private Account account;

}
