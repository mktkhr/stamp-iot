package com.example.stamp_app.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "マイコンデータ")
data class MicroControllerPostResponse(
	@Schema(description = "マイコンID", example = "1")
	private val id: Long,

	@Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
	private val uuid: String,

	@Schema(description = "マイコン名", example = "サンプル端末")
	private val name: String? = null,

	@Schema(description = "MACアドレス", example = "AA:AA:AA:AA:AA:AA")
	private val macAddress: String,

	@Schema(description = "測定間隔", example = "60")
	private val interval: String,

	@Schema(description = "SDI-12アドレス", example = "1,3")
	private val sdi12Address: String? = null,

	@Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
	private val createdAt: LocalDateTime,

	@Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
	private val updatedAt: LocalDateTime,

	@Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
	private val deletedAt: LocalDateTime?,
)
