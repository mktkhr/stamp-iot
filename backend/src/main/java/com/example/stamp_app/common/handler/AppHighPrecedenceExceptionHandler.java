package com.example.stamp_app.common.handler;

import com.example.stamp_app.common.enums.ErrorCode;
import com.example.stamp_app.domain.common.entity.AppResponseObject;
import com.example.stamp_app.domain.exception.EMSDatabaseException;
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException;
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppHighPrecedenceExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<AppResponseObject> handleConstraintViolationException(ConstraintViolationException exception) {

        var errorMessage = exception.getMessage() != null ? exception.getMessage() : ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.getMessage();

        log.error(errorMessage);

        var response = AppResponseObject.createErrorResponse(ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.getCode(), errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<AppResponseObject> handleIllegalAccessException(IllegalAccessException exception) {

        var errorMessage = exception.getMessage() != null ? exception.getMessage() : ErrorCode.ILLEGAL_ACCESS_EXCEPTION.getMessage();

        log.error(errorMessage);

        var response = AppResponseObject.createErrorResponse(ErrorCode.ILLEGAL_ACCESS_EXCEPTION.getCode(), errorMessage);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppResponseObject> handleIllegalArgumentException(IllegalArgumentException exception) {

        var errorMessage = exception.getMessage() != null ? exception.getMessage() : ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.getMessage();

        log.error(errorMessage);

        var response = AppResponseObject.createErrorResponse(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.getCode(), errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EMSDatabaseException.class)
    public ResponseEntity<AppResponseObject> handleEMSDatabaseException(EMSDatabaseException exception) {

        var errorMessage = exception.getMessage() != null ? exception.getMessage() : ErrorCode.EMS_DATABASE_EXCEPTION.getMessage();

        log.error(errorMessage);

        var response = AppResponseObject.createErrorResponse(ErrorCode.EMS_DATABASE_EXCEPTION.getCode(), errorMessage);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EMSResourceNotFoundException.class)
    public ResponseEntity<AppResponseObject> handleEMSResourceNotFoundException(EMSResourceNotFoundException exception) {

        var errorMessage = exception.getMessage() != null ? exception.getMessage() : ErrorCode.EMS_RESOURCE_NOT_FOUND_EXCEPTION.getMessage();

        log.error(errorMessage);

        var response = AppResponseObject.createErrorResponse(ErrorCode.EMS_RESOURCE_NOT_FOUND_EXCEPTION.getCode(), errorMessage);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EMSResourceDuplicationException.class)
    public ResponseEntity<AppResponseObject> handleEMSResourceDuplicationException(EMSResourceDuplicationException exception) {

        var errorMessage = exception.getMessage() != null ? exception.getMessage() : ErrorCode.EMS_RESOURCE_DUPLICATION_EXCEPTION.getMessage();

        log.error(errorMessage);

        var response = AppResponseObject.createErrorResponse(ErrorCode.EMS_RESOURCE_DUPLICATION_EXCEPTION.getCode(), errorMessage);

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
