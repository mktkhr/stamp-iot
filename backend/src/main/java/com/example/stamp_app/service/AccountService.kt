package com.example.stamp_app.service

import com.example.stamp_app.controller.param.account.LoginPostParam
import com.example.stamp_app.controller.param.account.RegisterPostParam
import com.example.stamp_app.controller.response.AccountGetResponse
import com.example.stamp_app.controller.response.AccountLoginResponse
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.entity.Account
import com.example.stamp_app.repository.AccountRepository
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.DigestUtils
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class])
class AccountService(
	private val accountRepository: AccountRepository
) {

	companion object {
		private val log = LoggerFactory.getLogger(AccountService::class.java)
	}

	/**
	 * アカウント追加Service
	 *
	 * @param registerPostParam 登録情報
	 */
	@Throws(EMSResourceDuplicationException::class)
	fun addAccount(registerPostParam: @NotNull RegisterPostParam) {
		val isNewUser = accountRepository.findByEmail(registerPostParam.email) == null

		if (!isNewUser) {
			log.error(" The email address has already been used.")
			throw EMSResourceDuplicationException("")
		}

		val hashedPassword = DigestUtils.md5DigestAsHex(registerPostParam.password.toByteArray())
		val localDateTime = LocalDateTime.now()
		val newUser = Account(
			null,
			UUID.randomUUID(),
			registerPostParam.email,
			hashedPassword,
			null,
			localDateTime,
			localDateTime,
			null,
			null
		)
		accountRepository.save(newUser)
	}

	/**
	 * ログインService
	 *
	 * @param loginPostParam ログイン情報
	 * @return HttpStatus
	 */
	@Throws(IllegalAccessException::class)
	fun login(loginPostParam: @NotNull LoginPostParam): AccountLoginResponse {
		val loginUser = accountRepository.findByEmailAndDeletedAtIsNull(loginPostParam.email)

		// 対象のアカウントが存在しない場合，404を返す
		if (loginUser == null) {
			log.error("This account does not exist.")
			throw EMSResourceNotFoundException("")
		}

		val isCorrectPassword =
			loginUser.password!!.matches(DigestUtils.md5DigestAsHex(loginPostParam.password.toByteArray()).toRegex())

		// パスワードが合致しない場合，401を返す
		if (!isCorrectPassword) {
			log.error("Account Information are not correct.")
			throw IllegalAccessException()
		}

		log.info("Successfully logged in.")
		return AccountLoginResponse(HttpStatus.OK, loginUser)
	}

	/**
	 * アカウント情報取得Service
	 *
	 * @param userUuid ユーザーUUID
	 * @return Account アカウント情報
	 */
	@Throws(IllegalArgumentException::class)
	fun getAccountInfo(userUuid: @NotNull String?): AccountGetResponse {
		val account = accountRepository.findByUuid(UUID.fromString(userUuid))

		// アカウントが存在しない場合，400を返す
		if (account == null || account.deletedAt != null) {
			log.error("This account does not exist.")
			throw IllegalArgumentException()
		}

		// IDと名前のみを返す
		return AccountGetResponse(
			account.id!!,
			account.name,
			account.createdAt!!,
			account.updatedAt!!
		)
	}

	/**
	 * アカウント削除Service
	 *
	 * @param userUuid ユーザーUUID
	 */
	@Throws(IllegalArgumentException::class)
	fun deleteAccount(userUuid: @NotNull String?) {
		val account = accountRepository.findByUuid(UUID.fromString(userUuid))

		// アカウントが存在しない場合，400を返す
		if (account == null) {
			log.error("This account does not exist.")
			throw IllegalArgumentException()
		}

		val updatedAccount = Account(
			account.id,
			account.uuid,
			account.email,
			account.password,
			account.name,
			account.createdAt,
			account.updatedAt,
			LocalDateTime.now(),  // 論理削除フラグとして削除日時を追加
			account.microController
		)

		// 論理削除したデータで上書き
		accountRepository.save(updatedAccount)
	}
}
