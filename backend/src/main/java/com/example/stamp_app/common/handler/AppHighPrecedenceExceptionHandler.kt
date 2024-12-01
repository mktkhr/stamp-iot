package com.example.stamp_app.common.handler

import com.example.stamp_app.common.enums.ErrorCode
import com.example.stamp_app.domain.common.entity.AppResponseObject
import com.example.stamp_app.domain.exception.EMSDatabaseException
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class AppHighPrecedenceExceptionHandler {

	companion object {
		private val log = LoggerFactory.getLogger(AppFinalExceptionHandler::class.java)
	}

	@ExceptionHandler
	fun handleConstraintViolationException(exception: ConstraintViolationException): ResponseEntity<AppResponseObject> {
		val errorMessage = if (exception.message != null) exception.message else ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.message
		log.error(errorMessage, exception)
		val response = AppResponseObject.createErrorResponse(ErrorCode.CONSTRAINT_VIOLATION_EXCEPTION.code, errorMessage)
		return ResponseEntity(response, HttpStatus.BAD_REQUEST)
	}

	@ExceptionHandler(IllegalAccessException::class)
	fun handleIllegalAccessException(exception: IllegalAccessException): ResponseEntity<AppResponseObject> {
		val errorMessage = if (exception.message != null) exception.message else ErrorCode.ILLEGAL_ACCESS_EXCEPTION.message
		log.error(errorMessage, exception)
		val response = AppResponseObject.createErrorResponse(ErrorCode.ILLEGAL_ACCESS_EXCEPTION.code, errorMessage)
		return ResponseEntity(response, HttpStatus.UNAUTHORIZED)
	}

	/**
	 * NOTE: 例外メッセージが存在しない場合は呼び出し箇所でstacktraceを出力すること
	 *
	 * @param exception 例外
	 * @return レスポンス
	 */
	@ExceptionHandler(IllegalArgumentException::class)
	fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<AppResponseObject> {
		val errorMessage = if (exception.message != null) exception.message else ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.message

		// 例外内容が存在する場合はここでstacktraceを出力
		if (Objects.nonNull(exception.message)) {
			log.error(errorMessage, exception)
		}
		val response = AppResponseObject.createErrorResponse(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.code, errorMessage)
		return ResponseEntity(response, HttpStatus.BAD_REQUEST)
	}

	/**
	 * TODO: JPA/Hibernate等のinterceptorを実装してそちらでDBアクセスの例外をcatchできるか確認する
	 *
	 * @param exception 例外
	 * @return レスポンス
	 */
	@ExceptionHandler(EMSDatabaseException::class)
	fun handleEMSDatabaseException(exception: EMSDatabaseException): ResponseEntity<AppResponseObject> {
		val errorMessage = if (exception.message != null) exception.message else ErrorCode.EMS_DATABASE_EXCEPTION.message
		log.error(errorMessage, exception)
		val response = AppResponseObject.createErrorResponse(ErrorCode.EMS_DATABASE_EXCEPTION.code, errorMessage)
		return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
	}

	@ExceptionHandler(EMSResourceNotFoundException::class)
	fun handleEMSResourceNotFoundException(exception: EMSResourceNotFoundException): ResponseEntity<AppResponseObject> {
		val errorMessage = if (exception.message != null) exception.message else ErrorCode.EMS_RESOURCE_NOT_FOUND_EXCEPTION.message
		log.error(errorMessage, exception)
		val response = AppResponseObject.createErrorResponse(ErrorCode.EMS_RESOURCE_NOT_FOUND_EXCEPTION.code, errorMessage)
		return ResponseEntity(response, HttpStatus.NOT_FOUND)
	}

	@ExceptionHandler(EMSResourceDuplicationException::class)
	fun handleEMSResourceDuplicationException(exception: EMSResourceDuplicationException): ResponseEntity<AppResponseObject> {
		val errorMessage = if (exception.message != null) exception.message else ErrorCode.EMS_RESOURCE_DUPLICATION_EXCEPTION.message
		log.error(errorMessage, exception)
		val response = AppResponseObject.createErrorResponse(ErrorCode.EMS_RESOURCE_DUPLICATION_EXCEPTION.code, errorMessage)
		return ResponseEntity(response, HttpStatus.FORBIDDEN)
	}
}
