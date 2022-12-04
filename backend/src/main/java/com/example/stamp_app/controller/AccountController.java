package com.example.stamp_app.controller;

import com.example.stamp_app.controller.Response.AccountInfoResponse;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.service.AccountService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.example.stamp_app.constants.Constants.SESSION_VALID_TIME_IN_SEC;

@Controller
@RequestMapping(value = "/ems/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;

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

        HttpStatus responseStatus = accountService.addAccount(userData);

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

        if (userData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Account loginUser = accountService.login(userData, httpServletResponse);

        if (loginUser != null) {
            // redisにセッション情報を追加
            String sessionId = UUID.randomUUID().toString();
            redisService.set(sessionId, loginUser.getUuid().toString(), SESSION_VALID_TIME_IN_SEC);

            // cookieを生成し，レスポンスにセット
            Cookie cookie = sessionService.generateCookie(sessionId);
            httpServletResponse.addCookie(cookie);

            System.out.println("<< Account Controller(login:POST)");
            return new ResponseEntity<>(HttpStatus.OK);
        }

        System.out.println("<< Account Controller(login:POST)");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * アカウント情報取得
     *
     * @return ユーザーIDとユーザー名
     */
    @GetMapping(value = "/info")
    public ResponseEntity<AccountInfoResponse> accountInfo(HttpServletRequest httpServletRequest) {
        var cookieLIst = httpServletRequest.getCookies();

        var sessionUuid = sessionService.getSessionUuidFromCookie(cookieLIst);

        var userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);

        var accountInfo = accountService.getAccountInfo(userUuid);

        if(accountInfo != null){
            return new ResponseEntity<>(accountInfo, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
