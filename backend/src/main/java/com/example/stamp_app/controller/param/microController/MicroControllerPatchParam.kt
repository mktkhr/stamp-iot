package com.example.stamp_app.controller.param.microController

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.springframework.validation.annotation.Validated
import java.util.*

@Schema(description = "マイコン更新データ")
@Validated
data class MicroControllerPatchParam(
	@Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
	val microControllerUuid: @NotNull UUID,

	@Schema(description = "マイコン名", example = "サンプル端末")
	val name: String,

	@Schema(description = "測定間隔", example = "60")
	val interval: @NotNull @Pattern(regexp = "^(60|30|20|15|10|5|1)$") String,

	@Nullable
	@Schema(description = "SDI-12アドレス", example = "1,3")
	val sdi12Address: @Pattern(regexp = "^((([0-9A-Za-z],)+[0-9A-za-z])|([0-9A-za-z])?)$") String? = null
)
