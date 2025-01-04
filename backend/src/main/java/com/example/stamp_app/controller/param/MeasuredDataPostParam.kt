package com.example.stamp_app.controller.param

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

@Schema(description = "測定結果登録パラメータ")
data class MeasuredDataPostParam(

	@field:NotBlank(message = "MacAddress must not be blank")
	@field:Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$", message = "MacAddress pattern is invalid")
	@Schema(description = "MACアドレス", example = "AA:AA:AA:AA:AA:AA")
	val macAddress: String,

	@Schema(description = "SDI-12パラメータリスト")
	val sdi12Param: List<Sdi12Param>,

	@Schema(description = "環境データパラメータリスト")
	val environmentalDataParam: List<EnvironmentalDataParam>,

	@Schema(description = "電圧", example = "11.11")
	val voltage: String

)