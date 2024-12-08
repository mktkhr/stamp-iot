package com.example.stamp_app.controller.response.measuredDataGetResponse

import com.example.stamp_app.entity.MeasuredDataMaster
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.Getter
import java.time.LocalDateTime

@Getter
@AllArgsConstructor
@Schema(description = "電圧データ")
data class VoltageDataGetResponse(
	@Schema(description = "測定データマスターID", example = "1")
	private val measuredDataMasterId: Long,

	@Schema(description = "DOY", example = "111.11")
	private val dayOfYear: String,

	@Schema(description = "電圧", example = "11.11")
	private val voltage: String? = null,

	@Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
	private val createdAt: LocalDateTime,

	@Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
	private val updatedAt: LocalDateTime,

	@Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
	private val deletedAt: LocalDateTime? = null,

	) {

	companion object {
		fun convertFromMeasuredDataMaster(measuredDataMaster: @NotNull MeasuredDataMaster): VoltageDataGetResponse {
			return VoltageDataGetResponse(
				measuredDataMaster.id!!,
				measuredDataMaster.dayOfYear,
				measuredDataMaster.voltage,
				measuredDataMaster.createdAt,
				measuredDataMaster.updatedAt,
				measuredDataMaster.deletedAt
			)
		}
	}
	
}
