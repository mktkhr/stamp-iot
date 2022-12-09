package com.example.stamp_app.controller.response;

import com.example.stamp_app.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class AccountLoginResponse {

    private HttpStatus status;

    private Account account;

}
