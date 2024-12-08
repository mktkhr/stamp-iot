package com.example.stamp_app.controller.response

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "アカウント情報")
data class AccountGetResponse(
	@Schema(description = "アカウントID", example = "1")
	val id: Long,
	@Schema(
		description = "登録名",
		example = "サンプル太郎"
	)
	val name: String?,
	@Schema(
		description = "作成日時",
		example = "2023-01-01T01:01:01.111111"
	)
	val createdAt: LocalDateTime,
	@Schema(
		description = "更新日時",
		example = "2023-01-01T01:01:01.111111"
	)
	val updatedAt: LocalDateTime
)
