package com.example.stamp_app.controller.param.microController

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import java.util.*

@Schema(description = "マイコン更新データ")
data class MicroControllerPatchParam(
	@Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
	@field:NotNull(message = "UUID must not be NULL")
	val microControllerUuid: UUID,

	@Schema(description = "マイコン名", example = "サンプル端末")
	val name: String,

	@Schema(description = "測定間隔", example = "60")
	@field:NotNull(message = "Interval must not be NULL")
	@field:Pattern(regexp = "^(60|30|20|15|10|5|1)$")
	val interval: String,

	@Nullable
	@Schema(description = "SDI-12アドレス", example = "1,3")
	@field:Pattern(
		regexp = "^((([0-9A-Za-z],)+[0-9A-Za-z])|([0-9A-Za-z])?)$",
		message = "SDI-12 address pattern is invalid"
	)
	val sdi12Address: String? = null
)
