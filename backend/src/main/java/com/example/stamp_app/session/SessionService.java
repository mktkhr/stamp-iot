package com.example.stamp_app.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public class SessionService {
    @Autowired
    RedisService redisService;

    public boolean checkSession(String userUlid, String sessionId) {
        try {
            String sessionValue = redisService.get(userUlid);
            return sessionValue.matches(sessionId);
        } catch (Exception e) {
            return false;
        }
    }

    public Cookie generateCookie(String sessionId) {
        Cookie cookie = new Cookie("ems_session", sessionId);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
