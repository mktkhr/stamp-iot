package com.example.stamp_app.service.micro_controller

import com.example.stamp_app.controller.response.MicroControllerGetResponse
import com.example.stamp_app.domain.exception.EMSResourceDuplicationException
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.entity.MicroController
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.MicroControllerService
import com.example.stamp_app.service.TestBase
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Transactional
class MicroControllerServiceTest : TestBase() {
	@Autowired
	private lateinit var microControllerService: MicroControllerService

	@Autowired
	protected lateinit var requestedUser: RequestedUser

	@Nested
	inner class AddMicroControllerRelationTest {

		@Test
		@DisplayName("存在しないMACアドレスに対してリレーションを追加しようとした場合，エラーになること")
		fun `case1-1`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case1.xml")

			assertThrows<EMSResourceNotFoundException> {
				microControllerService.addMicroControllerRelation(
					1L,
					"BB:BB:BB:BB:BB:BB"
				)
			}
		}

		@Test
		@DisplayName("存在しないユーザーに対してリレーションを追加しようとした場合，エラーになること")
		fun `case1-2`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case1.xml")

			assertThrows<EMSResourceNotFoundException> {
				microControllerService.addMicroControllerRelation(
					2L,
					"AA:AA:AA:AA:AA:AA"
				)
			}
		}

		@Test
		@DisplayName("ユーザーもMicroControllerを存在しているが，MicroControllerにアカウントが既に紐づいている場合，エラーになること")
		fun `case1-3`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case1.xml")

			assertThrows<EMSResourceDuplicationException> {
				microControllerService.addMicroControllerRelation(
					1L,
					"AA:AA:AA:AA:AA:AA"
				)
			}
		}

		@Test
		@DisplayName("ユーザーもMicroControllerも存在し，MicroControllerにアカウントが紐づいているない場合，正常に紐づきが")
		fun `case1-4`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case1-4.xml")

			assertDoesNotThrow {
				microControllerService.addMicroControllerRelation(
					1L,
					"BB:BB:BB:BB:BB:BB"
				)
			}
		}

	}

	@Nested
	inner class GetMicroControllerListTest {

		@Test
		@DisplayName("存在しないユーザーに対して取得しようとした場合，エラーになること")
		fun `case2-1`() {
			assertThrows<EMSResourceNotFoundException> {
				microControllerService.getMicroControllerList("8ea20e98-043d-4117-8a24-771351bce045")
			}
		}

		@Test
		@DisplayName("ユーザーに紐づくMicroControllerが存在しない場合，空配列が返ること")
		fun `case2-2`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case2.xml")

			val microControllerList =
				microControllerService.getMicroControllerList("8ea20e98-043d-4117-8a24-771351bce044")

			Assertions.assertEquals(listOf<MicroControllerGetResponse>(), microControllerList)
		}

		@Test
		@DisplayName("ユーザーに複数のMicroControllerが紐づいている場合，すべてが返ること")
		fun `case2-3`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case2.xml")

			val microControllerList =
				microControllerService.getMicroControllerList("8ea20e98-043d-4117-8a24-771351bce045")

			Assertions.assertEquals(2, microControllerList.size)

			val expectedList = listOf(
				MicroControllerGetResponse(
					1,
					"1ea20e98-043d-4117-8a24-771351bce045",
					"テスト端末",
					"AA:AA:AA:AA:AA:AA",
					"60",
					"1",
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
				),
				MicroControllerGetResponse(
					2,
					"2ea20e98-043d-4117-8a24-771351bce045",
					"テスト端末B",
					"BB:BB:BB:BB:BB:BB",
					"60",
					"1,2,3",
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
				)
			)

			Assertions.assertEquals(expectedList, microControllerList)
		}

	}

	@Nested
	inner class GetMicroControllerDetailTest {

		@Test
		@DisplayName("UUIDのフォーマットで指定されていない場合，IllegalArgumentExceptionが発生すること")
		fun `case3-1`() {
			assertThrows<IllegalArgumentException> {
				microControllerService.getMicroControllerDetail("1")
			}
		}

		@Test
		@DisplayName("存在しないUUIDを指定した場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case3-2`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case3.xml")

			assertThrows<EMSResourceNotFoundException> {
				microControllerService.getMicroControllerDetail("99999999-043d-4117-8a24-771351bce045")
			}
		}

		@Test
		@DisplayName("アカウントの紐付けが存在しないMicroControllerのUUIDを指定した場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case3-3`() {
			loadDatasetAndInsert("src/test/resources/service/micro_controller/case3.xml")

			assertThrows<EMSResourceNotFoundException> {
				microControllerService.getMicroControllerDetail("2ea20e98-043d-4117-8a24-771351bce045")
			}
		}

		@Test
		@DisplayName("MicroControllerの所有者とリクエストユーザーが異なる場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case3-4`() {

			requestedUser.userUuid = "11111111-043d-4117-8a24-771351bce045"

			loadDatasetAndInsert("src/test/resources/service/micro_controller/case3.xml")

			assertThrows<EMSResourceNotFoundException> {
				microControllerService.getMicroControllerDetail("1ea20e98-043d-4117-8a24-771351bce045")
			}
		}

		@Test
		@DisplayName("MicroControllerの所有者とリクエストユーザーが一致する場合，正常に取得できること")
		fun `case3-5`() {

			requestedUser.userUuid = "8ea20e98-043d-4117-8a24-771351bce045"

			loadDatasetAndInsert("src/test/resources/service/micro_controller/case3.xml")

			val microControllerDetail =
				microControllerService.getMicroControllerDetail("1ea20e98-043d-4117-8a24-771351bce045")

			val expectedMicroController = MicroController(
				1,
				UUID.fromString("1ea20e98-043d-4117-8a24-771351bce045"),
				"テスト端末",
				"AA:AA:AA:AA:AA:AA",
				"60",
				"1",
				LocalDateTime.parse("2025-01-01T01:00:00.000"),
				LocalDateTime.parse("2025-01-01T01:00:00.000"),
				null,
				null,
				null
			)

			// NOTE: accountとmeasuredMasterを含めるとStackOverflowが発生するため，nullに変更する
			val ignoreAccountAndMeasuredDataMaster =
				microControllerDetail.copy(account = null, measuredDataMasters = null)

			Assertions.assertEquals(expectedMicroController, ignoreAccountAndMeasuredDataMaster)
		}

	}

	@Nested
	inner class GetMicroControllerDetailWithMacAddressTest {

		@Test
		@DisplayName("存在しないMacAddressが指定された場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case4-1`() {
			assertThrows<EMSResourceNotFoundException> {
				microControllerService.getMicroControllerDetailWithMacAddress("BB:BB:BB:BB:BB:BB")
			}
		}

		@Test
		@DisplayName("存在するMacAddressが指定された場合，正常に取得できること")
		fun `case4-2`() {

			loadDatasetAndInsert("src/test/resources/service/micro_controller/case4.xml")

			val microControllerDetail =
				microControllerService.getMicroControllerDetailWithMacAddress("AA:AA:AA:AA:AA:AA")

			val expectedMicroController = MicroController(
				1,
				UUID.fromString("1ea20e98-043d-4117-8a24-771351bce045"),
				"テスト端末",
				"AA:AA:AA:AA:AA:AA",
				"60",
				"1",
				LocalDateTime.parse("2025-01-01T01:00:00.000"),
				LocalDateTime.parse("2025-01-01T01:00:00.000"),
				null,
				null,
				null
			)

			// NOTE: accountとmeasuredMasterを含めるとStackOverflowが発生するため，nullに変更する
			val ignoreAccountAndMeasuredDataMaster =
				microControllerDetail.copy(account = null, measuredDataMasters = null)

			Assertions.assertEquals(expectedMicroController, ignoreAccountAndMeasuredDataMaster)
		}

	}
}