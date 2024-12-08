package com.example.stamp_app.controller.response.measuredDataGetResponse

import com.example.stamp_app.entity.EnvironmentalData
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import lombok.AllArgsConstructor
import lombok.Getter
import java.time.LocalDateTime

@Getter
@AllArgsConstructor
@Schema(description = "環境データ")
data class EnvironmentalDataGetResponse(
	@Schema(description = "測定データマスターID", example = "1")
	private val measuredDataMasterId: Long,

	@Schema(description = "DOY", example = "111.11")
	private val dayOfYear: String,

	@Schema(description = "大気圧", example = "1011.11")
	private val airPress: String? = null,

	@Schema(description = "気温", example = "11.11")
	private val temp: String? = null,

	@Schema(description = "相対湿度", example = "77.77")
	private val humi: String? = null,

	@Schema(description = "二酸化炭素濃度", example = "1111.11")
	private val co2Concent: String? = null,

	@Schema(description = "総揮発性有機化合物", example = "11.11")
	private val tvoc: String? = null,

	@Schema(description = "アナログ値", example = "11.11")
	private val analogValue: String? = null,

	@Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
	private val createdAt: LocalDateTime,

	@Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
	private val updatedAt: LocalDateTime,

	@Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
	private val deletedAt: LocalDateTime? = null
) {
	companion object {
		fun convertFromEnvironmentalData(
			environmentalData: @NotNull EnvironmentalData,
			measuredDataMasterId: @NotNull Long,
			dayOfYear: @NotNull String
		): EnvironmentalDataGetResponse {
			return EnvironmentalDataGetResponse(
				measuredDataMasterId,
				dayOfYear,
				environmentalData.airPress,
				environmentalData.temp,
				environmentalData.humi,
				environmentalData.co2Concent,
				environmentalData.tvoc,
				environmentalData.analogValue,
				environmentalData.measuredDataMaster.createdAt,
				environmentalData.measuredDataMaster.updatedAt,
				environmentalData.measuredDataMaster.deletedAt
			)
		}
	}
}

