package com.example.stamp_app.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
public class AppServletFilter extends OncePerRequestFilter {

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // リクエスト
        requestWrapper.getParameterNames();
        log.info("@ >>>>>Request: " + request.getMethod() + " " + request.getRequestURI());

        // リクエストボディ
        String requestBody = new String(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        if (!requestBody.isEmpty()) {
            log.info("@ Request Body: " + requestBody);
        }

        // リクエストパラメータ
        StringBuilder builder = new StringBuilder();
        Enumeration<?> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {

            // パラメータ名を取得
            String paramName = (String) paramNames.nextElement();
            builder.append(paramName);
            builder.append("=");

            // パラメータ値を取得
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                if (i > 0) {
                    builder.append(",");
                }
                builder.append(paramValues[i]);
            }

            builder.append(",");
        }
        if (!builder.isEmpty()) {
            log.info("@ Request Param: " + builder);
        }

        // メインの処理
        filterChain.doFilter(requestWrapper, responseWrapper);

        // レスポンス
        log.info("@ <<<<<Request: " + request.getMethod() + " " + request.getRequestURI());
        responseWrapper.copyBodyToResponse();
    }

}
