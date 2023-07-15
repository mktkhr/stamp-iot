package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.account.LoginPostParam;
import com.example.stamp_app.controller.param.account.RegisterPostParam;
import com.example.stamp_app.controller.response.AccountGetResponse;
import com.example.stamp_app.controller.response.AccountLoginResponse;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.AccountService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.stamp_app.constants.Constants.SESSION_VALID_TIME_IN_SEC;

@RestController
@RequestMapping(value = "/ems/account")
public class AccountController {
    @Autowired
    AccountService accountService;
    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;
    @Autowired
    RequestedUser requestedUser;

    /**
     * アカウント登録API
     *
     * @param registerPostParam 登録情報
     * @return ResponseEntity
     */
    @PostMapping(value = "/register")
    public ResponseEntity<HttpStatus> addAccount(@RequestBody @Validated RegisterPostParam registerPostParam) {

        accountService.addAccount(registerPostParam);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * ログインAPI
     *
     * @param loginPostParam 登録情報
     * @return ResponseEntity
     */
    @PostMapping(value = "/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Validated LoginPostParam loginPostParam, HttpServletResponse httpServletResponse) {

        AccountLoginResponse accountLoginResponse = accountService.login(loginPostParam);

        // redisにセッション情報を追加
        String sessionId = UUID.randomUUID().toString();
        redisService.set(sessionId, accountLoginResponse.getAccount().getUuid().toString(), SESSION_VALID_TIME_IN_SEC);

        // cookieを生成し，レスポンスにセット
        Cookie cookie = sessionService.generateCookie(sessionId);
        httpServletResponse.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * ログアウトAPI
     *
     * @return ResponseEntity
     */
    @PostMapping(value = "/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletResponse httpServletResponse) {

        // redis からセッション情報を削除
        redisService.delete(requestedUser.getSessionUuid());

        // 有効期限切れのCookieをレスポンスにセット
        httpServletResponse.addCookie(sessionService.generateExpiredCookie(requestedUser.getSessionUuid()));

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * アカウント情報取得
     *
     * @return ユーザーIDとユーザー名
     */
    @GetMapping(value = "/info")
    public ResponseEntity<AccountGetResponse> accountInfo() {

        var userUuid = redisService.getUserUuidFromSessionUuid(requestedUser.getSessionUuid());

        var accountGetResponse = accountService.getAccountInfo(userUuid);

        return new ResponseEntity<>(accountGetResponse, HttpStatus.OK);
    }
}
