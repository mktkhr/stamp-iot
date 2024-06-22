package com.example.stamp_app.common.handler;

import com.example.stamp_app.common.enums.ErrorCode;
import com.example.stamp_app.domain.common.entity.AppResponseObject;
import com.example.stamp_app.domain.common.entity.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Catch できなかった例外を全て Catch するハンドラ
 */
@Slf4j
@RestControllerAdvice
public class AppFinalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<AppResponseObject> handleUncaughtException(Throwable throwable) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var errorMessage = throwable.getMessage() != null ? throwable.getMessage() : ErrorCode.EMS_UNCAUGHT_EXCEPTION.getMessage();

        log.error(errorMessage, throwable);

        var apiResponse = new AppResponseObject();
        apiResponse.addError(new Error(ErrorCode.EMS_UNCAUGHT_EXCEPTION.getCode(), ErrorCode.EMS_UNCAUGHT_EXCEPTION.getMessage()));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
