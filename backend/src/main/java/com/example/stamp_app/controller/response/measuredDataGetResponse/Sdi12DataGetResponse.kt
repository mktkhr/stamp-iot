package com.example.stamp_app.controller.response.measuredDataGetResponse

import com.example.stamp_app.entity.Sdi12Data
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "SDI-12データ")
data class Sdi12DataGetResponse(
	@Schema(description = "SDI-12アドレス", example = "1")
	val sdiAddress: String?,

	@Schema(description = "SDI-12測定データとDOYリスト")
	val dataList: MutableList<Sdi12DataAndDoy?>
) {
	companion object {
		fun convertFromSdi12Data(
			sdi12Data: @NotNull Sdi12Data,
			measuredDataMasterId: @NotNull Long,
			dayOfYear: @NotNull String
		): Sdi12DataAndDoy {
			return Sdi12DataAndDoy(
				measuredDataMasterId,
				dayOfYear,
				sdi12Data.vwc,
				sdi12Data.soilTemp,
				sdi12Data.brp,
				sdi12Data.sbec,
				sdi12Data.spwec,
				sdi12Data.gax,
				sdi12Data.gay,
				sdi12Data.gaz,
				sdi12Data.measuredDataMaster.createdAt,
				sdi12Data.measuredDataMaster.updatedAt,
				sdi12Data.measuredDataMaster.deletedAt
			)
		}
	}
}

data class Sdi12DataAndDoy(
	@Schema(description = "測定データマスターID", example = "1")
	val measuredDataMasterId: Long? = null,

	@Schema(description = "DOY", example = "111.11")
	val dayOfYear: String? = null,

	@Schema(description = "体積含水率", example = "11.11")
	val vwc: String? = null,

	@Schema(description = "地温", example = "11.11")
	val soilTemp: String? = null,

	@Schema(description = "比誘電率", example = "11.11")
	val brp: String? = null,

	@Schema(description = "電気伝導度", example = "11.11")
	val sbec: String? = null,

	@Schema(description = "間隙水電気伝導度", example = "11.11")
	val spwec: String? = null,

	@Schema(description = "重力加速度(X)", example = "1.11")
	val gax: String? = null,

	@Schema(description = "重力加速度(Y)", example = "1.11")
	val gay: String? = null,

	@Schema(description = "重力加速度(Z)", example = "1.11")
	val gaz: String? = null,

	@Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
	val createdAt: LocalDateTime? = null,

	@Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
	val updatedAt: LocalDateTime? = null,

	@Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
	val deletedAt: LocalDateTime? = null
)
