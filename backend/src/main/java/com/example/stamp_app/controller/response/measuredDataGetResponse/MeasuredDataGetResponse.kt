package com.example.stamp_app.controller.response.measuredDataGetResponse

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "測定結果取得レスポンス")
data class MeasuredDataGetResponse(
	@Schema(description = "SDI-12データリスト")
	val sdi12Data: List<Sdi12DataGetResponse>,

	@Schema(description = "環境データリスト")
	val environmentalData: List<EnvironmentalDataGetResponse>,

	@Schema(description = "電圧データ")
	val voltageData: List<VoltageDataGetResponse>,
)
