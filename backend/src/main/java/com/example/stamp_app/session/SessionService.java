package com.example.stamp_app.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

import static com.example.stamp_app.constants.Constants.*;

@Service
public class SessionService {
    @Autowired
    RedisService redisService;

    public Cookie generateCookie(String sessionId) {
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(SESSION_VALID_TIME_IN_SEC);
        cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * Cookie配列に含まれているEMSのセッション情報を取得
     *
     * @param cookies リクエストに含まれていたCookie配列
     * @return セッションUUID
     */
    public String getSessionUuidFromCookie(Cookie[] cookies){
        String sessionUuid = null;

        if(cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    sessionUuid = cookie.getValue();
                }
            }
        }

        return sessionUuid;
    }
}
