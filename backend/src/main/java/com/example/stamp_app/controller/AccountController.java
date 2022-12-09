package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.account.LoginPostParam;
import com.example.stamp_app.controller.param.account.RegisterPostParam;
import com.example.stamp_app.controller.response.AccountGetResponse;
import com.example.stamp_app.controller.response.AccountLoginResponse;
import com.example.stamp_app.service.AccountService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
     * @param registerPostParam 登録情報
     * @return ResponseEntity
     */
    @PostMapping(value = "/register")
    public ResponseEntity<HttpStatus> addAccount(@RequestBody @Valid RegisterPostParam registerPostParam) {
        System.out.println(">> Account Controller(register:POST)");
        System.out.println("RequestBody:" + registerPostParam);

        accountService.addAccount(registerPostParam);

        System.out.println("<< Account Controller(register:POST)");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * ログインAPI
     *
     * @param loginPostParam 登録情報
     * @return ResponseEntity
     */
    @PostMapping(value = "/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginPostParam loginPostParam, HttpServletResponse httpServletResponse) {
        System.out.println(">> Account Controller(login:POST)");
        System.out.println("RequestBody:" + loginPostParam);

        if (loginPostParam == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        AccountLoginResponse accountLoginResponse = accountService.login(loginPostParam);

        // redisにセッション情報を追加
        String sessionId = UUID.randomUUID().toString();
        redisService.set(sessionId, accountLoginResponse.getAccount().getUuid().toString(), SESSION_VALID_TIME_IN_SEC);

        // cookieを生成し，レスポンスにセット
        Cookie cookie = sessionService.generateCookie(sessionId);
        httpServletResponse.addCookie(cookie);

        System.out.println("<< Account Controller(login:POST)");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * アカウント情報取得
     *
     * @return ユーザーIDとユーザー名
     */
    @GetMapping(value = "/info")
    public ResponseEntity<AccountGetResponse> accountInfo(HttpServletRequest httpServletRequest) {
        var cookieLIst = httpServletRequest.getCookies();

        var sessionUuid = sessionService.getSessionUuidFromCookie(cookieLIst);
        if(sessionUuid == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);
        if(userUuid == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var accountGetResponse = accountService.getAccountInfo(userUuid);

        return new ResponseEntity<>(accountGetResponse, accountGetResponse.getStatus());
    }
}
