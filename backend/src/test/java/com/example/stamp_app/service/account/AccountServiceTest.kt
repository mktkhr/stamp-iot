package com.example.stamp_app.service.account

import com.example.stamp_app.controller.param.account.LoginPostParam
import com.example.stamp_app.controller.param.account.RegisterPostParam
import com.example.stamp_app.controller.response.AccountGetResponse
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.service.AccountService
import com.example.stamp_app.service.TestBase
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class AccountServiceTest : TestBase() {

	@Autowired
	private lateinit var accountService: AccountService

	@Nested
	inner class GetAccountTest {

		@Test
		@DisplayName("存在するUUIDのアカウントを取得した際，期待通り取得できること")
		fun `case1-1`() {

			val expectedAccount = AccountGetResponse(
				1L,
				"TestAccount",
				LocalDateTime.parse("2025-01-01T01:00"),
				LocalDateTime.parse("2025-01-01T01:00")
			)

			loadDatasetAndInsert("src/test/resources/service/account/case1.xml")

			val account = accountService.getAccountInfo("8ea20e98-043d-4117-8a24-771351bce045")

			assertEquals(expectedAccount, account)
		}

		@Test
		@DisplayName("存在しないUUIDのアカウントを取得した際，期待通り取得できること")
		fun `case1-2`() {

			loadDatasetAndInsert("src/test/resources/service/account/case1.xml")

			assertThrows<IllegalArgumentException> { accountService.getAccountInfo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa") }
		}

	}

	@Nested
	inner class DeleteAccountTest {

		@Test
		@DisplayName("存在するUUIDのアカウントを削除した場合，正常に削除できること")
		fun `case2-1`() {

			val expectedAccount = AccountGetResponse(
				1L,
				"TestAccount",
				LocalDateTime.parse("2025-01-01T01:00"),
				LocalDateTime.parse("2025-01-01T01:00")
			)

			loadDatasetAndInsert("src/test/resources/service/account/case2.xml")

			val account = accountService.getAccountInfo("8ea20e98-043d-4117-8a24-771351bce045")

			assertEquals(expectedAccount, account)

			accountService.deleteAccount("8ea20e98-043d-4117-8a24-771351bce045")

			assertThrows<IllegalArgumentException> { accountService.getAccountInfo("8ea20e98-043d-4117-8a24-771351bce045") }
		}

		@Test
		@DisplayName("存在しないUUIDのアカウントを削除しようとした場合，エラーになること")
		fun `case2-2`() {

			loadDatasetAndInsert("src/test/resources/service/account/case2.xml")

			assertThrows<IllegalArgumentException> { accountService.deleteAccount("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa") }
		}

		@Test
		@DisplayName("すでに論理削除済みのアカウントを削除しようとした場合，エラーになること")
		fun `case2-3`() {

			loadDatasetAndInsert("src/test/resources/service/account/case2-3.xml")

			assertThrows<IllegalArgumentException> { accountService.deleteAccount("8ea20e98-043d-4117-8a24-771351bce045") }
		}

	}

	@Nested
	inner class LoginTest {

		@Test
		@DisplayName("Paramのemailに該当するアカウントが存在しない場合，エラーになること")
		fun `case3-1`() {

			val loginPostParam = LoginPostParam(
				"test@example.com",
				"TestTest"
			)

			assertThrows<EMSResourceNotFoundException> { accountService.login(loginPostParam) }

		}

		@Test
		@DisplayName("Paramのemailに該当するアカウントが存在するが，論理削除済みの場合，エラーになること")
		fun `case3-2`() {

			loadDatasetAndInsert("src/test/resources/service/account/case3-2.xml")

			val loginPostParam = LoginPostParam(
				"test@example.com",
				"TestTest"
			)

			assertThrows<EMSResourceNotFoundException> { accountService.login(loginPostParam) }
		}

		@Test
		@DisplayName("Paramのemailに該当するアカウントが存在するが，パスワードが一致しない場合，エラーになること")
		fun `case3-3`() {

			loadDatasetAndInsert("src/test/resources/service/account/case3.xml")

			val loginPostParam = LoginPostParam(
				"test@example.com",
				"HogeHoge"
			)

			assertThrows<IllegalAccessException> { accountService.login(loginPostParam) }
		}

		@Test
		@DisplayName("Paramのemailに該当するアカウントが存在し，パスワードも一致する場合，正常にログインできること")
		fun `case3-4`() {

			loadDatasetAndInsert("src/test/resources/service/account/case2-3.xml")

			assertThrows<IllegalArgumentException> { accountService.deleteAccount("8ea20e98-043d-4117-8a24-771351bce045") }
		}

	}

	@Nested
	inner class AddAccountTest {

		@Test
		@DisplayName("同一のEmailのアカウントが既に存在する場合，エラーになること")
		fun `case4-1`() {

			loadDatasetAndInsert("src/test/resources/service/account/case4.xml")

			val registerPostParam = RegisterPostParam(
				"test@example.com",
				"TestTest"
			)

			assertThrows<EMSResourceDuplicationException> { accountService.addAccount(registerPostParam) }
		}

		@Test
		@DisplayName("Emailが重複しないアカウントの場合，正常に登録できること")
		fun `case4-2`() {

			val registerPostParam = RegisterPostParam(
				"test@example.com",
				"TestTest"
			)

			assertDoesNotThrow { accountService.addAccount(registerPostParam) }

			val expectedAccount = AccountGetResponse(
				1L,
				"TestAccount",
				LocalDateTime.parse("2025-01-01T01:00"),
				LocalDateTime.parse("2025-01-01T01:00")
			)

			val savedAccount = accountService.getAccountInfo("8ea20e98-043d-4117-8a24-771351bce045")

			// NOTE: created_at, updated_atの差分を吸収する
			val ignoreDates = savedAccount.copy(
				createdAt = LocalDateTime.parse("2025-01-01T01:00"),
				updatedAt = LocalDateTime.parse("2025-01-01T01:00")
			)

			assertEquals(expectedAccount, savedAccount)
		}

	}

}
