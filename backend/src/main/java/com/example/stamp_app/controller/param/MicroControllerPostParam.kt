package com.example.stamp_app.controller.param

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive

@Schema(description = "マイコン登録パラメータ")
data class MicroControllerPostParam(
	@Schema(description = "ユーザーID")
	@field:NotNull
	@field:Positive
	val userId: Long,

	@Schema(description = "MACアドレス")
	@field:NotBlank
	@field:Pattern(regexp = "^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$")
	val macAddress: String
)
