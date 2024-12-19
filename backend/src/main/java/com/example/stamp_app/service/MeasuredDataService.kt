package com.example.stamp_app.service

import com.example.stamp_app.controller.param.MeasuredDataPostParam
import com.example.stamp_app.controller.response.measuredDataGetResponse.EnvironmentalDataGetResponse
import com.example.stamp_app.controller.response.measuredDataGetResponse.MeasuredDataGetResponse
import com.example.stamp_app.controller.response.measuredDataGetResponse.Sdi12DataGetResponse
import com.example.stamp_app.controller.response.measuredDataGetResponse.VoltageDataGetResponse
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.entity.EnvironmentalData
import com.example.stamp_app.entity.MeasuredDataMaster
import com.example.stamp_app.entity.Sdi12Data
import com.example.stamp_app.repository.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class MeasuredDataService(
	private val microControllerRepository: MicroControllerRepository,
	private val measuredDataMasterRepository: MeasuredDataMasterRepository,
	private val sdi12DataRepository: Sdi12DataRepository,
	private val environmentalDataRepository: EnvironmentalDataRepository,
	private val sensorRepository: SensorRepository
) {

	companion object {
		private val log = LoggerFactory.getLogger(MeasuredDataService::class.java)
	}

	/**
	 * 測定データ追加Service
	 *
	 * @param measuredDataPostParam 測定データ
	 */
	@Transactional(rollbackFor = [Exception::class])
	@Throws(ResponseStatusException::class)
	fun addMeasuredData(measuredDataPostParam: @NotNull MeasuredDataPostParam) {

		// マイコンと所有者の一致確認
		log.info("測定データ送信元MACアドレス: " + measuredDataPostParam.macAddress)
		val microController = microControllerRepository.findByMacAddress(measuredDataPostParam.macAddress)

		// DBに登録されていないマイコンの場合
		if (microController == null) {
			log.error("登録されていないマイコン")
			throw EMSResourceNotFoundException("")
		}

		// 所有者UUIDがnullの場合401を返す
		if (microController.account?.uuid == null) {
			log.error("所有者の不一致")
			throw EMSResourceNotFoundException("") // NOTE: 所有者の有無を秘匿するため，404で返す
		}

		val accountId = microController.account.uuid.toString()
		log.info("測定データ登録対象のアカウントID: $accountId")

		// 測定時刻，DOYの算出
		val measuredTime = LocalDateTime.now()
		val doy = LocalDate.now().dayOfYear.toFloat()
		val hour = measuredTime.hour.toFloat()
		val minutes = measuredTime.minute.toFloat()
		val seconds = measuredTime.second.toFloat()
		val doyFloat = doy - 1 + hour / 24 + minutes / 1440 + seconds / 1440 / 60
		val doyForData = doyFloat.toString()

		// パラメータの取り出し
		val sdi12ParamList = measuredDataPostParam.sdi12Param
		val environmentalDataList = measuredDataPostParam.environmentalDataParam

		// 測定データマスターを作成
		val measuredDataMaster = MeasuredDataMaster(
			null,
			doyForData,
			measuredTime,
			measuredTime,
			null,
			microController,
			null,
			null,
			measuredDataPostParam.voltage
		)

		// 測定データの保存
		measuredDataMasterRepository.save(measuredDataMaster)

		// SDI-12データの保存
		for (sdi12Param in sdi12ParamList) {
			if (sdi12Param.sensorId == null) {
				continue
			}

			val sensor = sensorRepository.findById(sdi12Param.sensorId)
			val sdi12Data = Sdi12Data(
				null,
				sdi12Param.sdiAddress,
				sdi12Param.sdiAddress,
				sdi12Param.soilTemp,
				sdi12Param.brp,
				sdi12Param.sbec,
				sdi12Param.spwec,
				sdi12Param.gax,
				sdi12Param.gay,
				sdi12Param.gaz,
				measuredDataMaster,
				sensor
			)
			sdi12DataRepository.save(sdi12Data)
		}

		// 環境データの保存
		for (environmentalDataParam in environmentalDataList) {
			val environmentalData = EnvironmentalData(
				null,
				environmentalDataParam.airPress,
				environmentalDataParam.temp,
				environmentalDataParam.humi,
				environmentalDataParam.co2Concent,
				environmentalDataParam.tvoc,
				environmentalDataParam.analogValue,
				measuredDataMaster
			)
			environmentalDataRepository.save(environmentalData)
		}
	}

	/**
	 * マイコンIDを指定して，対象の測定結果を取得
	 *
	 * @param userUuid            　ユーザーID
	 * @param microControllerUuid マイコンUUID
	 * @return 測定結果リスト
	 */
	fun getMeasuredData(userUuid: @NotBlank String?, microControllerUuid: @NotBlank String?): MeasuredDataGetResponse {

		// マイコンが存在しない場合，400を返す
		val microController = microControllerRepository.findByUuid(UUID.fromString(microControllerUuid))
			?: throw EMSResourceNotFoundException("")

		// マイコン保有者IDとパラメータ内のユーザーIDが異なる場合，403を返す
		if (microController.account!!.uuid != UUID.fromString(userUuid)) {
			throw EMSResourceNotFoundException("")
		}

		// SDI-12の測定データ成形
		val sdi12DataGetResponseList: MutableList<Sdi12DataGetResponse> = ArrayList()

		// アカウントとマイコンIDに紐づくSDI-12アドレスのリストを取得する(マイコンに紐づくアカウントが変更された場合を考慮)
		val sdi12AddressList =
			sdi12DataRepository.findSdiAddressGroupBySdiAddress(UUID.fromString(userUuid), microController.id)

		// TODO: クエリを複数回呼んでいて明らかにパフォーマンスが悪いので修正する
		if (!sdi12AddressList.isNullOrEmpty()) {

			// アドレスforeach
			for (sdi12Address: String in sdi12AddressList) {
				// アドレスを指定してデータを取得
				val sdi12DataList = sdi12DataRepository.findBySdiAddress(sdi12Address) ?: continue

				// マイコンIDとユーザーUUIDがリクエストと一致するデータを取得し，Sdi12DataAndDoyに変換
				val convertedSdi12MeasuredDataList = sdi12DataList
					.stream()
					.filter { measuredData: Sdi12Data ->
						measuredData.measuredDataMaster.sdi12Data != null &&
							measuredData.measuredDataMaster.id != null &&
							measuredData.measuredDataMaster.dayOfYear != null &&
							measuredData.measuredDataMaster.microController.id == microController.id &&
							measuredData.measuredDataMaster.microController.account!!.uuid.toString() == userUuid
					}
					.map { measuredData: Sdi12Data ->
						if (measuredData.measuredDataMaster.id != null && measuredData.measuredDataMaster.dayOfYear != null) {
							return@map Sdi12DataGetResponse.convertFromSdi12Data(
								measuredData,
								measuredData.measuredDataMaster.id,
								measuredData.measuredDataMaster.dayOfYear
							)
						}

						return@map null

					}
					.filter { item -> item != null }
					.toList()

				val sdi12DataGetResponse = Sdi12DataGetResponse(
					sdi12Address,
					convertedSdi12MeasuredDataList
				)
				sdi12DataGetResponseList.add(sdi12DataGetResponse)
			}
		}


		// 環境データを全取得
		val environmentalDataList = environmentalDataRepository.findAll()

		// アカウントIDとマイコンIDに紐づく環境データを取得
		val convertedEnvironmentalMeasuredDataList = environmentalDataList
			.stream()
			.filter { measuredData: EnvironmentalData ->
				measuredData.measuredDataMaster.microController.id == microController.id &&
					measuredData.measuredDataMaster.microController.account!!.uuid.toString() == userUuid
			}
			.map { data: EnvironmentalData ->
				EnvironmentalDataGetResponse.convertFromEnvironmentalData(
					data,
					data.measuredDataMaster.id!!,
					data.measuredDataMaster.dayOfYear
				)
			}
			.toList()

		// 電圧データ成形
		// TODO: 全件取得していてパフォーマンスに問題があるので修正する
		val voltageDataList = measuredDataMasterRepository.findAll()

		val convertedVoltageMeasuredDataList = voltageDataList
			.stream()
			.filter { measuredDataMaster: MeasuredDataMaster ->
				measuredDataMaster.microController.id == microController.id &&
					microController.account.uuid.toString() == userUuid
			}
			.map { measuredDataMaster: MeasuredDataMaster ->
				VoltageDataGetResponse.convertFromMeasuredDataMaster(measuredDataMaster)
			}
			.toList()

		return MeasuredDataGetResponse(
			sdi12DataGetResponseList,
			convertedEnvironmentalMeasuredDataList,
			convertedVoltageMeasuredDataList
		)
	}
}
