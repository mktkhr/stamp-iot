package com.example.stamp_app.controller

import com.example.stamp_app.constants.Constants.SESSION_VALID_TIME_IN_SEC
import com.example.stamp_app.controller.param.account.LoginPostParam
import com.example.stamp_app.controller.param.account.RegisterPostParam
import com.example.stamp_app.controller.response.AccountGetResponse
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.AccountService
import com.example.stamp_app.session.RedisService
import com.example.stamp_app.session.SessionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.ObjectUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Account", description = "アカウント関連API")
@RequestMapping(value = ["/ems/account"])
open class AccountController(
	private val accountService: AccountService,
	private val sessionService: SessionService,
	private val redisService: RedisService,
	private val requestedUser: RequestedUser
) {

	/**
	 * アカウント登録API
	 *
	 * @param registerPostParam 登録情報
	 * @return ResponseEntity
	 */
	@Operation(summary = "アカウント登録API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "登録成功",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		), ApiResponse(
			responseCode = "400",
			description = "バリデーションエラー",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@PostMapping(value = ["/register"])
	fun addAccount(
		@RequestBody(
			description = "登録情報",
			content = [Content(schema = Schema(implementation = RegisterPostParam::class))]
		) @org.springframework.web.bind.annotation.RequestBody @Validated registerPostParam: RegisterPostParam?
	): ResponseEntity<HttpStatus> {
		accountService.addAccount(registerPostParam!!)
		return ResponseEntity(HttpStatus.OK)
	}

	/**
	 * ログインAPI
	 *
	 * @param loginPostParam 登録情報
	 * @return ResponseEntity
	 */
	@Operation(summary = "ログインAPI")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "ログイン成功",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		), ApiResponse(
			responseCode = "400",
			description = "バリデーションエラー",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		), ApiResponse(
			responseCode = "401",
			description = "認証エラー",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@PostMapping(value = ["/login"])
	@Throws(
		IllegalAccessException::class
	)
	fun login(
		@RequestBody(
			description = "ログイン情報",
			content = [Content(schema = Schema(implementation = LoginPostParam::class))]
		) @org.springframework.web.bind.annotation.RequestBody @Validated loginPostParam: LoginPostParam?,
		httpServletResponse: HttpServletResponse
	): ResponseEntity<HttpStatus> {
		val (_, account) = accountService.login(loginPostParam!!)

		// redisにセッション情報を追加
		val sessionId = UUID.randomUUID().toString()
		redisService[sessionId, account.uuid.toString()] = SESSION_VALID_TIME_IN_SEC.toLong()

		// cookieを生成し，レスポンスにセット
		val cookie = sessionService.generateCookie(sessionId)
		httpServletResponse.addCookie(cookie)
		return ResponseEntity(HttpStatus.OK)
	}

	/**
	 * ログアウトAPI
	 *
	 * @return ResponseEntity
	 */
	@Operation(summary = "ログアウトAPI")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "ログアウト成功",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@PostMapping(value = ["/logout"])
	fun logout(httpServletResponse: HttpServletResponse): ResponseEntity<HttpStatus> {

		// redis からセッション情報を削除
		redisService.delete(requestedUser.sessionUuid)

		// 有効期限切れのCookieをレスポンスにセット
		httpServletResponse.addCookie(sessionService.generateExpiredCookie(requestedUser.sessionUuid))
		return ResponseEntity(HttpStatus.OK)
	}

	/**
	 * アカウント情報取得API
	 *
	 * @return ユーザーIDとユーザー名
	 */
	@Operation(summary = "アカウント詳細取得API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "ログイン成功",
			content = [Content(
				mediaType = "application/json",
				schema = Schema(implementation = AccountGetResponse::class)
			)]
		), ApiResponse(
			responseCode = "400",
			description = "バリデーションエラー",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@GetMapping(value = ["/info"])
	fun accountInfo(): ResponseEntity<AccountGetResponse> {
		val userUuid = redisService.getUserUuidFromSessionUuid(requestedUser.sessionUuid)
		val accountGetResponse = accountService.getAccountInfo(userUuid)
		return ResponseEntity(accountGetResponse, HttpStatus.OK)
	}

	/**
	 * アカウント削除API(論理削除)
	 *
	 * @return ResponseEntity
	 */
	@Operation(summary = "アカウント削除API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "削除成功",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		), ApiResponse(
			responseCode = "400",
			description = "バリデーションエラー",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@DeleteMapping(value = ["/delete"])
	fun logicalDeleteAccount(httpServletResponse: HttpServletResponse): ResponseEntity<HttpStatus> {
		val userUuid = redisService.getUserUuidFromSessionUuid(requestedUser.sessionUuid)
		accountService.deleteAccount(userUuid)

		// redis からセッション情報を削除
		redisService.delete(requestedUser.sessionUuid)

		// 有効期限切れのCookieをレスポンスにセット
		httpServletResponse.addCookie(sessionService.generateExpiredCookie(requestedUser.sessionUuid))
		return ResponseEntity(HttpStatus.OK)
	}
}
