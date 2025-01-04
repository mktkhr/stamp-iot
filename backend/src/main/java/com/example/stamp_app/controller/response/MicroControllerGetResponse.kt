package com.example.stamp_app.controller.response

import com.example.stamp_app.entity.MicroController
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "マイコン詳細データ")
data class MicroControllerGetResponse(
	@Schema(description = "マイコンID", example = "1")
	val id: Long,

	@Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
	val uuid: String,

	@Schema(description = "マイコン名", example = "サンプル端末")
	val name: String? = null,

	@Schema(description = "MACアドレス", example = "AA:AA:AA:AA:AA:AA")
	val macAddress: String,

	@Schema(description = "測定間隔", example = "60")
	val interval: String,

	@Schema(description = "SDI-12アドレス", example = "1,3")
	val sdi12Address: String? = null,

	@Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
	val createdAt: LocalDateTime,

	@Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
	val updatedAt: LocalDateTime


) {
	companion object {
		fun convertMicroControllerToListResponse(
			microControllerList: List<MicroController>
		): List<MicroControllerGetResponse> {

			val microControllerGetResponseList: MutableList<MicroControllerGetResponse> = ArrayList()
			microControllerList.forEach { microController: MicroController ->
				val microControllerGetResponse = MicroControllerGetResponse(
					microController.id!!,
					microController.uuid.toString(),
					microController.name,
					microController.macAddress,
					microController.interval,
					microController.sdi12Address,
					microController.createdAt!!,
					microController.updatedAt!!
				)
				microControllerGetResponseList.add(microControllerGetResponse)
			}
			return microControllerGetResponseList
		}

		fun convertMicroControllerToDetailResponse(microController: MicroController): MicroControllerGetResponse {
			return MicroControllerGetResponse(
				microController.id!!,
				microController.uuid.toString(),
				microController.name,
				microController.macAddress,
				microController.interval,
				microController.sdi12Address,
				microController.createdAt!!,
				microController.updatedAt!!
			)
		}
	}
}
