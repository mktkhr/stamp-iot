package com.example.stamp_app.common.interceptor

import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.session.RedisService
import com.example.stamp_app.session.SessionService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.constraints.NotNull
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor

@Slf4j
class AppInterceptor : HandlerInterceptor {

	companion object {
		private val log = LoggerFactory.getLogger(AppInterceptor::class.java)
	}

	@Autowired
	var sessionService: SessionService? = null

	@Autowired
	var redisService: RedisService? = null

	@Autowired
	var requestedUser: RequestedUser? = null

	@Value("\${spring.profiles.active}")
	var activeProfile: String? = null

	/**
	 * Cookieを基に，セッションの有無を確認する
	 */
	@Throws(ResponseStatusException::class)
	override fun preHandle(
		request: HttpServletRequest,
		response: @NotNull HttpServletResponse,
		handler: @NotNull Any
	): Boolean {
		val path = request.requestURI

		// アクティブプロファイルがdevかつspringdoc用エンドポイントへのアクセスの場合
		if (activeProfile == "dev" && (path.contains("/swagger-ui") || path.contains("/api-docs"))) {
			return true
		}

		// セッションの有無を確認しないリクエスト
		if (path.contains("/login") || path.contains("/register") || path.contains("/session")) {
			return true
		}
		if (path.contains("/measured-data") && request.method == HttpMethod.POST.name()) {
			return true
		}
		if (path.contains("/micro-controller/detail/no-session") && request.method == HttpMethod.GET.name()) {
			return true
		}
		val cookieList = request.cookies
		val sessionUuid = sessionService!!.getSessionUuidFromCookie(cookieList)
		if (sessionUuid == null) {
			log.error("セッションの取得に失敗")
			throw ResponseStatusException(HttpStatus.BAD_REQUEST)
		}
		val userUuid = redisService!!.getUserUuidFromSessionUuid(sessionUuid)
		if (userUuid != null) {
			requestedUser!!.sessionUuid = sessionUuid
			requestedUser!!.userUuid = userUuid
			log.info(requestedUser.toString())
			response.status = HttpStatus.OK.value()
			return true
		}
		log.error("セッションが無効なリクエスト")
		throw ResponseStatusException(HttpStatus.BAD_REQUEST)
	}
}
