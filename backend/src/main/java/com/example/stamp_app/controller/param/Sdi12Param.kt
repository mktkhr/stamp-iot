package com.example.stamp_app.controller.param

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "SDI-12パラメータ")
data class Sdi12Param(
	@Schema(description = "センサID", example = "1")
	val sensorId: Long?,

	@Schema(description = "SDI-12アドレス", example = "1")
	val sdiAddress: @NotNull String,

	@Schema(description = "体積含水率", example = "11.11")
	val vwc: String?,

	@Schema(description = "地温", example = "11.11")
	val soilTemp: String?,

	@Schema(description = "比誘電率", example = "11.11")
	val brp: String?,

	@Schema(description = "電気伝導度", example = "11.11")
	val sbec: String?,

	@Schema(description = "間隙水電気伝導度", example = "11.11")
	val spwec: String?,

	@Schema(description = "重力加速度(X)", example = "1.11")
	val gax: String?,

	@Schema(description = "重力加速度(Y)", example = "1.11")
	val gay: String?,

	@Schema(description = "重力加速度(Z)", example = "1.11")
	val gaz: String?
)
