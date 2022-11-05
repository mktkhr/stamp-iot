package com.example.stamp_app.controller;

import com.example.stamp_app.entity.Account;
import com.example.stamp_app.service.LoginService;
import com.example.stamp_app.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/ems/account")
public class AccountController {
    @Autowired
    RegisterService registerService;
    @Autowired
    LoginService loginService;

    /**
     * アカウント登録API
     *
     * @param userData 登録情報
     * @return ResponseEntity
     */
    @PostMapping(value = "/register")
    public ResponseEntity<HttpStatus> addAccount(@RequestBody Account userData) {
        System.out.println(">> Account Controller(register:POST)");
        System.out.println("RequestBody:" + userData);

        HttpStatus responseStatus = registerService.addAccount(userData);
        System.out.println("<< Account Controller(register:POST)");
        return new ResponseEntity<>(responseStatus);
    }

    /**
     * ログインAPI
     *
     * @param userData 登録情報
     * @return ResponseEntity
     */
    @PostMapping(value = "/login")
    public ResponseEntity<HttpStatus> login(@RequestBody Account userData, HttpServletResponse httpServletResponse) {
        System.out.println(">> Account Controller(login:POST)");
        System.out.println("RequestBody:" + userData);

        HttpStatus responseStatus = loginService.login(userData, httpServletResponse);
        System.out.println("<< Account Controller(login:POST)");
        return new ResponseEntity<>(responseStatus);
    }
}
