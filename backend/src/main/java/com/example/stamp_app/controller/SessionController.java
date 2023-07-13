package com.example.stamp_app.controller;

import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<String> addAccount(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        var cookieList = request.getCookies();

        String sessionUuid = sessionService.getSessionUuidFromCookie(cookieList);

        if (sessionUuid == null) {
            return new ResponseEntity<>("failed", HttpStatus.OK);
        }

        String UserUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);

        if (UserUuid == null) {
            return new ResponseEntity<>("failed", HttpStatus.OK);
        } else {
            // 有効期限が更新されたCookieを生成してレスポンスに追加
            Cookie cookie = sessionService.generateCookie(sessionUuid);
            httpServletResponse.addCookie(cookie);

            return new ResponseEntity<>("success", HttpStatus.OK);
        }
    }
}
