package com.example.stamp_app.controller.param.account

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

@Schema(description = "アカウント登録パラメータ")
data class RegisterPostParam(
	@field:NotNull(message = "Email must not be NULL")
	@field:Pattern(
		regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$",
		message = "Email pattern is invalid"
	)
	@Schema(description = "メールアドレス", example = "aaa@example.com")
	val email: String,

	@field:NotNull(message = "Password must not be NULL")
	@field:Pattern(regexp = "^(?=.*[A-Z])[a-zA-Z0-9.?/-]{8,24}$", message = "Password pattern must not be NULL")
	@Schema(description = "パスワード", example = "SamplePassword")
	val password: String
)
