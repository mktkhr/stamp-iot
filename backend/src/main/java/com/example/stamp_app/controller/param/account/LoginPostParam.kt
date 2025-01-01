package com.example.stamp_app.controller.param.account

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Schema(description = "ログインパラメータ")
data class LoginPostParam(
	@field:NotBlank(message = "Email must not be NULL")
	@field:Pattern(
		regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$",
		message = "Email pattern is invalid"
	)
	@Schema(description = "メールアドレス", example = "aaa@example.com")
	val email: String,

	// NOTE: ログイン時のパスワードはパターン推測を回避するため，パターンバリデーションを行わない
	@field:NotBlank(message = "Password must not be NULL")
	@Schema(description = "パスワード", example = "SamplePassword")
	val password: String
)
