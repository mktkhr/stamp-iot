package com.example.stamp_app.common.interceptor;

import com.example.stamp_app.domain.exception.EMSDatabaseException;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Slf4j
public class AppInterceptor implements HandlerInterceptor {

    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;
    @Autowired
    RequestedUser requestedUser;
    @Value("${spring.profiles.active}")
    String activeProfile;

    /**
     * Cookieを基に，セッションの有無を確認する
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws ResponseStatusException {

        var path = request.getRequestURI();

        // アクティブプロファイルがdevかつspringdoc用エンドポイントへのアクセスの場合
        if (Objects.equals(activeProfile, "dev") && (path.contains("/swagger-ui") || path.contains("/api-docs"))) {
            return true;
        }

        // セッションの有無を確認しないリクエスト
        if (path.contains("/login") || path.contains("/register") || path.contains("/session")) {
            return true;
        }

        if (path.contains("/measured-data") && Objects.equals(request.getMethod(), HttpMethod.POST.name())) {
            return true;
        }

        if (path.contains("/micro-controller/detail/no-session") && Objects.equals(request.getMethod(), HttpMethod.GET.name())) {
            return true;
        }

        var cookieList = request.getCookies();

        var sessionUuid = sessionService.getSessionUuidFromCookie(cookieList);

        if (sessionUuid == null) {
            log.error("セッションの取得に失敗");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String userUuid;

        try {
            userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EMSDatabaseException();
        }

        if (userUuid != null) {
            requestedUser.setSessionUuid(sessionUuid);
            requestedUser.setUserUuid(userUuid);
            log.info(requestedUser.toString());
            response.setStatus(HttpStatus.OK.value());
            return true;
        }

        log.error("セッションが無効なリクエスト");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
