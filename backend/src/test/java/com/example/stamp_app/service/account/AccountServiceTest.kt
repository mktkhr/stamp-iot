package com.example.stamp_app.service.account

import com.example.stamp_app.controller.response.AccountGetResponse
import com.example.stamp_app.service.AccountService
import com.example.stamp_app.service.TestBase
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@Rollback
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

}
