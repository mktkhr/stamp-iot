package com.example.stamp_app.service

import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam
import com.example.stamp_app.controller.response.MicroControllerGetResponse
import com.example.stamp_app.controller.response.MicroControllerPostResponse
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.entity.MicroController
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.repository.AccountRepository
import com.example.stamp_app.repository.MicroControllerRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Slf4j
class MicroControllerService(
	private val microControllerRepository: MicroControllerRepository,
	private val accountRepository: AccountRepository,
	private val requestedUser: RequestedUser
) {

	companion object {
		private val log = LoggerFactory.getLogger(MicroControllerService::class.java)
	}

	fun addMicroControllerRelation(userId: Long, macAddress: String): MicroControllerPostResponse {

		// 対象のMAC Addressのマイクロコントローラー情報を取得
		val microController = microControllerRepository.findByMacAddress(macAddress)
			?: throw EMSResourceNotFoundException("")

		// 対象のマイクロコントローラーがDB上に存在しなかった場合，404を返す

		// リクエストしたアカウント情報を取得
		val requestedAccount = accountRepository.findById(userId) ?: throw EMSResourceNotFoundException("")

		// 既にマイクロコントローラーがアカウントに紐づけられていた場合，401を返す
		if (microController.account != null) {
			throw EMSResourceDuplicationException("")
		}

		val updatedMicroController = MicroController(
			microController.id,
			microController.uuid,
			microController.name,
			microController.macAddress,
			microController.interval,
			microController.sdi12Address,
			microController.createdAt,
			microController.updatedAt,
			microController.deletedAt,
			microController.measuredDataMasters,
			requestedAccount // 紐付けを更新
		)

		// マイクロコントローラー情報を更新する
		microControllerRepository.save(updatedMicroController)
		return MicroControllerPostResponse(
			microController.id!!,
			microController.uuid.toString(),
			microController.name,
			microController.macAddress,
			microController.interval,
			microController.sdi12Address,
			microController.createdAt!!,
			microController.updatedAt!!,
			microController.deletedAt!!
		)
	}

	/**
	 * アカウントに紐づくマイコンを取得
	 *
	 * @param userUuid ユーザーID
	 * @return アカウントに紐づくマイコンリスト
	 */
	fun getMicroControllerList(userUuid: String): List<MicroControllerGetResponse> {
		val account = accountRepository.findByUuid(UUID.fromString(userUuid))

		// アカウントが存在しなかった場合，404を返す
		if (account == null) {
			log.error("アカウントが存在しない")
			throw EMSResourceNotFoundException("")
		}
		val microControllerList = account.microController
		return MicroControllerGetResponse.convertMicroControllerToListResponse(microControllerList ?: listOf())
	}

	/**
	 * マイコン詳細を取得
	 *
	 * @param microControllerUuid マイコンID
	 * @return マイコン詳細
	 */
	fun getMicroControllerDetail(microControllerUuid: String): MicroController {
		val microController = microControllerRepository.findByUuid(UUID.fromString(microControllerUuid))

		// マイコンが存在しなかった場合，400を返す
		if (microController == null) {
			log.error("該当のマイクロコントローラーの取得に失敗 UUID: $microControllerUuid")
			throw EMSResourceNotFoundException("")
		}

		// マイコン所有者とリクエストユーザーが異なる場合，404を返す
		if (microController.account == null || microController.account.uuid.toString() != requestedUser.userUuid) {
			log.error("マイコン所有者とリクエストユーザーの不一致")
			throw EMSResourceNotFoundException("")
		}
		return microController
	}

	/**
	 * マイコン詳細を取得(MACアドレス)
	 *
	 * @param macAddress マイコンID
	 * @return マイコン詳細
	 */
	fun getMicroControllerDetailWithMacAddress(macAddress: String): MicroController {
		val microController = microControllerRepository.findByMacAddress(macAddress)

		// マイコンが存在しなかった場合，404を返す
		if (microController == null) {
			log.error("該当のマイクロコントローラーの取得に失敗 MACアドレス: $macAddress")
			throw EMSResourceNotFoundException("")
		}
		return microController
	}

	/**
	 * マイコン詳細を更新
	 *
	 * @param param 更新用パラメータ
	 * @return 更新後のマイコン詳細
	 */
	@Throws(EMSResourceNotFoundException::class)
	@Transactional(rollbackFor = [Exception::class])
	fun updateMicroControllerDetail(userUuid: String, param: MicroControllerPatchParam): MicroController {
		val microController = microControllerRepository.findByUuid(param.microControllerUuid)

		// マイコンが存在しなかった場合，404を返す
		if (microController == null) {
			log.error("該当のマイクロコントローラーの取得に失敗 マイコンUUID: " + param.microControllerUuid)
			throw EMSResourceNotFoundException("")
		}

		// マイコン所有者とリクエストしたアカウントが一致しない場合，404を返す
		if (microController.account!!.uuid.toString() != userUuid) {
			log.error("マイコン所有者とリクエストユーザーの不一致 マイコンUUID: " + param.microControllerUuid)
			throw EMSResourceNotFoundException("")
		}
		val updateTargetMicroController = MicroController(
			microController.id,
			microController.uuid,
			if (Objects.nonNull(param.name)) param.name else microController.name,
			microController.macAddress,
			param.interval,
			if (Objects.nonNull(param.sdi12Address)) param.sdi12Address else microController.sdi12Address,
			microController.createdAt,
			LocalDateTime.now(),
			microController.deletedAt,
			microController.measuredDataMasters,
			microController.account
		)
		microControllerRepository.save(updateTargetMicroController)

		return microControllerRepository.findByUuid(param.microControllerUuid)
			?: throw EMSResourceNotFoundException("マイコンの更新に失敗しました。")
	}
}
