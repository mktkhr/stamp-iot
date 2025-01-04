package com.example.stamp_app.session

import com.example.stamp_app.constants.Constants
import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SessionService(
		private val redisService: RedisService
) {

	@Value("\${domain}")
	val domain: String? = null

	/**
	 * Cookieを生成する
	 *
	 * @param sessionId 登録するセッションID
	 * @return 生成したcookie
	 */
	fun generateCookie(sessionId: String?): Cookie {

		val cookie = Cookie(Constants.COOKIE_NAME, sessionId)
		cookie.domain = domain
		cookie.path = "/"
		cookie.maxAge = Constants.SESSION_VALID_TIME_IN_SEC
		cookie.isHttpOnly = true

		return cookie
	}

	/**
	 * 期限切れのCookieを生成する
	 *
	 * @param sessionId 登録するセッションID
	 * @return 生成したcookie
	 */
	fun generateExpiredCookie(sessionId: String?): Cookie {

		val cookie = Cookie(Constants.COOKIE_NAME, sessionId)
		cookie.domain = domain
		cookie.path = "/"
		cookie.maxAge = 0
		cookie.isHttpOnly = true

		return cookie
	}

	/**
	 * Cookie配列に含まれているEMSのセッション情報を取得
	 *
	 * @param cookies リクエストに含まれていたCookie配列
	 * @return セッションUUID
	 */
	fun getSessionUuidFromCookie(cookies: Array<Cookie>?): String? {
		var sessionUuid: String? = null

		if (cookies != null) {
			for (cookie in cookies) {
				if (cookie.name == Constants.COOKIE_NAME) {
					sessionUuid = cookie.value
				}
			}
		}

		return sessionUuid
	}
}
