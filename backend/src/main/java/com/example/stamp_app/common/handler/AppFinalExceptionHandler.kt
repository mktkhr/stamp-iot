package com.example.stamp_app.common.handler

import com.example.stamp_app.common.enums.ErrorCode
import com.example.stamp_app.domain.common.entity.AppResponseObject
import com.example.stamp_app.domain.common.entity.Error
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Catch できなかった例外を全て Catch するハンドラ
 */
@RestControllerAdvice
class AppFinalExceptionHandler : ResponseEntityExceptionHandler() {

	companion object {
		private val log = LoggerFactory.getLogger(AppFinalExceptionHandler::class.java)
	}

	@ExceptionHandler(Throwable::class)
	fun handleUncaughtException(throwable: Throwable): ResponseEntity<AppResponseObject> {

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		val errorMessage = if (throwable.message != null) throwable.message else ErrorCode.EMS_UNCAUGHT_EXCEPTION.message
		log.error(errorMessage, throwable)
		val apiResponse = AppResponseObject()
		apiResponse.addError(Error(ErrorCode.EMS_UNCAUGHT_EXCEPTION.code, ErrorCode.EMS_UNCAUGHT_EXCEPTION.message))
		headers.contentType = MediaType.APPLICATION_JSON

		return ResponseEntity(apiResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR)
	}
}
