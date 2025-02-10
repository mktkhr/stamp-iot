package com.example.stamp_app.service.micro_controller

import com.example.stamp_app.domain.exception.EMSResourceDuplicationException
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.service.MicroControllerService
import com.example.stamp_app.service.TestBase
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class MicroControllerServiceTest : TestBase() {
	@Autowired
	private lateinit var microControllerService: MicroControllerService

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
}