package com.example.stamp_app.controller.param

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "環境データパラメータ")
data class EnvironmentalDataParam(
	@Schema(description = "大気圧", example = "1011.11")
	val airPress: String? = null,

	@Schema(description = "気温", example = "11.11")
	val temp: String? = null,

	@Schema(description = "相対湿度", example = "77.77")
	val humi: String? = null,

	@Schema(description = "二酸化炭素濃度", example = "1111.11")
	val co2Concent: String? = null,

	@Schema(description = "総揮発性有機化合物", example = "11.11")
	val tvoc: String? = null,

	@Schema(description = "アナログ値", example = "11.11")
	val analogValue: String? = null
)
