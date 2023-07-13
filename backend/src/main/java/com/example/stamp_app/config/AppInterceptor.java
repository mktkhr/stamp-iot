package com.example.stamp_app.config;

import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class AppInterceptor implements HandlerInterceptor {

    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;
    @Autowired
    RequestedUser requestedUser;

    /**
     * Cookieを基に，セッションの有無を確認する
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws ResponseStatusException {

        var path = request.getRequestURI();

        // セッションの有無を確認しないリクエスト
        if (path.contains("/login") || path.contains("/register") || path.contains("/session")) {
            return true;
        }

        var cookieList = request.getCookies();

        var sessionUuid = sessionService.getSessionUuidFromCookie(cookieList);

        try {
            var userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);
            if (userUuid != null) {
                requestedUser.setSessionUuid(sessionUuid);
                requestedUser.setUserUuid(userUuid);
                response.setStatus(HttpStatus.OK.value());
                return true;
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return false;
    }
}
