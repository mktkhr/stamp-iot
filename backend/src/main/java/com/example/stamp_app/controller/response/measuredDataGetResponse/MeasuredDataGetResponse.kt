package com.example.stamp_app.controller.response.measuredDataGetResponse

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "測定結果取得レスポンス")
data class MeasuredDataGetResponse(
	@Schema(description = "SDI-12データリスト")
	private val sdi12Data: List<Sdi12DataGetResponse>,

	@Schema(description = "環境データリスト")
	private val environmentalData: List<EnvironmentalDataGetResponse>,

	@Schema(description = "電圧データ")
	private val voltageData: List<VoltageDataGetResponse>,
)
