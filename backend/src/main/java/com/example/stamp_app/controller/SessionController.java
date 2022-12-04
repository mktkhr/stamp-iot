package com.example.stamp_app.controller;

import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/ems/session")
public class SessionController {

    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;

    /**
     * セッションの有効確認API
     *
     * @param request リクエスト内容
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<HttpStatus> addAccount(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        System.out.println(">> Session Controller(POST)");

        var cookieList = request.getCookies();

        String sessionUuid = sessionService.getSessionUuidFromCookie(cookieList);

        String UserUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);

        System.out.println("<< Session Controller(POST)");
        if (UserUuid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // 有効期限が更新されたCookieを生成してレスポンスに追加
            Cookie cookie = sessionService.generateCookie(sessionUuid);
            httpServletResponse.addCookie(cookie);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
