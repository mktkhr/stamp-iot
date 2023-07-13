package com.example.stamp_app.session;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.example.stamp_app.constants.Constants.COOKIE_NAME;
import static com.example.stamp_app.constants.Constants.SESSION_VALID_TIME_IN_SEC;

@Service
public class SessionService {
    @Autowired
    RedisService redisService;
    @Value("${domain}")
    public String domain;

    /**
     * Cookieを生成する
     *
     * @param sessionId 登録するセッションID
     * @return 生成したcookie
     */
    public Cookie generateCookie(String sessionId) {
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(SESSION_VALID_TIME_IN_SEC);
        cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * 期限切れのCookieを生成する
     *
     * @param sessionId 登録するセッションID
     * @return 生成したcookie
     */
    public Cookie generateExpiredCookie(String sessionId) {
        Cookie cookie = new Cookie(COOKIE_NAME, sessionId);
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setMaxAge(0);
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
