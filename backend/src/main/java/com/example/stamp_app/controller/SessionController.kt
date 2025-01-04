package com.example.stamp_app.controller

import com.example.stamp_app.session.RedisService
import com.example.stamp_app.session.SessionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Session", description = "セッション関連API")
@RequestMapping(value = ["/ems/session"])
class SessionController(
	private val sessionService: SessionService,
	private val redisService: RedisService
) {

	/**
	 * セッションの有効確認API
	 *
	 * @param request リクエスト内容
	 * @return ResponseEntity
	 */
	@Operation(summary = "セッション確認API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "確認成功",
			content = [Content(
				examples = [ExampleObject(
					name = "セッション有効",
					value = "success"
				), ExampleObject(name = "セッション無効", value = "failed")]
			)]
		)]
	)
	@PostMapping
	fun checkSession(request: HttpServletRequest, httpServletResponse: HttpServletResponse): ResponseEntity<String> {
		val cookieList = request.cookies
		val sessionUuid = sessionService.getSessionUuidFromCookie(cookieList)
			?: return ResponseEntity("failed", HttpStatus.OK)

		val userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid)
		return if (userUuid == null) {
			ResponseEntity("failed", HttpStatus.OK)
		} else {
			// 有効期限が更新されたCookieを生成してレスポンスに追加
			val cookie = sessionService.generateCookie(sessionUuid)
			httpServletResponse.addCookie(cookie)
			ResponseEntity("success", HttpStatus.OK)
		}
	}
}
