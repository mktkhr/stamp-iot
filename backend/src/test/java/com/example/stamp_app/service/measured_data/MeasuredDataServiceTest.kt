package com.example.stamp_app.service.measured_data

import com.example.stamp_app.controller.param.EnvironmentalDataParam
import com.example.stamp_app.controller.param.MeasuredDataPostParam
import com.example.stamp_app.controller.param.Sdi12Param
import com.example.stamp_app.controller.response.measuredDataGetResponse.EnvironmentalDataGetResponse
import com.example.stamp_app.controller.response.measuredDataGetResponse.Sdi12DataAndDoy
import com.example.stamp_app.controller.response.measuredDataGetResponse.Sdi12DataGetResponse
import com.example.stamp_app.controller.response.measuredDataGetResponse.VoltageDataGetResponse
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException
import com.example.stamp_app.service.MeasuredDataService
import com.example.stamp_app.service.TestBase
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
class MeasuredDataServiceTest : TestBase() {
	@Autowired
	private lateinit var measuredDataService: MeasuredDataService

	@Nested
	inner class AddMeasuredDataTest {

		private val sdi12Param = Sdi12Param(
			1L,
			"1",
			"12.34",
			"1.23",
			"2.34",
			"3.45",
			"4.56",
			"1.234",
			"2.345",
			"3.456"
		)

		private val environmentalParam = EnvironmentalDataParam(
			"1234.56",
			"12.34",
			"23.45",
			"3456",
			"4567",
			"567"
		)

		@Test
		@DisplayName("存在しないMicroControllerに対する登録の場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case1-1`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case1.xml")

			val noMicroControllerParam = MeasuredDataPostParam(
				"CC:CC:CC:CC:CC:CC",
				listOf(sdi12Param),
				listOf(environmentalParam),
				"1"
			)

			assertThrows<EMSResourceNotFoundException> { measuredDataService.addMeasuredData(noMicroControllerParam) }
		}

		@Test
		@DisplayName("アカウントに紐づかないMicroControllerに対する登録の場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case1-2`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case1.xml")

			val noAccountRelationMicroControllerParam = MeasuredDataPostParam(
				"BB:BB:BB:BB:BB:BB",
				listOf(sdi12Param),
				listOf(environmentalParam),
				"1"
			)

			assertThrows<EMSResourceNotFoundException> {
				measuredDataService.addMeasuredData(
					noAccountRelationMicroControllerParam
				)
			}
		}

		@Test
		@DisplayName("存在するMicroControllerに対する登録の場合，正常に登録できること")
		fun `case1-3`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case1.xml")

			val param = MeasuredDataPostParam(
				"AA:AA:AA:AA:AA:AA",
				listOf(sdi12Param),
				listOf(environmentalParam),
				"1"
			)

			assertDoesNotThrow {
				measuredDataService.addMeasuredData(param)
			}
		}

	}
}