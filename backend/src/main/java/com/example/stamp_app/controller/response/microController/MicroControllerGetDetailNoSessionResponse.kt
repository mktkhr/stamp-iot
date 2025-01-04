package com.example.stamp_app.controller.response.microController

import com.example.stamp_app.entity.MicroController
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "マイコン詳細データ(セッション無し)")
data class MicroControllerGetDetailNoSessionResponse(
	@Schema(description = "測定間隔", example = "60")
	private val interval: String,

	@Schema(description = "SDI-12アドレス", example = "1,3")
	private val sdi12Address: String? = null
) {
	companion object {
		fun convertMicroControllerToDetailResponse(microController: MicroController): MicroControllerGetDetailNoSessionResponse {
			return MicroControllerGetDetailNoSessionResponse(
				microController.interval,
				microController.sdi12Address
			)
		}
	}
}
