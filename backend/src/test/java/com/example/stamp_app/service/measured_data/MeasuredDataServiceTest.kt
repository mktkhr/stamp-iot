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

	@Nested
	inner class GetMeasuredDataTest {

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
		@DisplayName("存在しないMicroControllerに対する取得の場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case2-1`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case2.xml")

			assertThrows<EMSResourceNotFoundException> {
				measuredDataService.getMeasuredData(
					"8ea20e98-043d-4117-8a24-771351bce045",
					"99999999-043d-4117-8a24-771351bce045"
				)
			}
		}

		@Test
		@DisplayName("存在しないユーザーのリクエストの場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case2-2`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case2.xml")

			assertThrows<EMSResourceNotFoundException> {
				measuredDataService.getMeasuredData(
					"99999999-043d-4117-8a24-771351bce045",
					"1ea20e98-043d-4117-8a24-771351bce045"
				)
			}
		}

		@Test
		@DisplayName("MicroControllerの所有者とリクエストユーザーが異なる場合，EMSResourceNotFoundExceptionが発生すること")
		fun `case2-3`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case2.xml")

			assertThrows<EMSResourceNotFoundException> {
				measuredDataService.getMeasuredData(
					"8ea20e98-043d-4117-8a24-771351bce046",
					"1ea20e98-043d-4117-8a24-771351bce045"
				)
			}
		}

		@Test
		@DisplayName("1つのMeasuredMasterに2つのSDI12アドレスのセンサが紐づいている場合，すべて取得できること")
		fun `case2-4`() {
			loadDatasetAndInsert("src/test/resources/service/measured_data/case2.xml")

			val measuredDataGetResponse = measuredDataService.getMeasuredData(
				"8ea20e98-043d-4117-8a24-771351bce045",
				"1ea20e98-043d-4117-8a24-771351bce045"
			)

			Assertions.assertEquals(2, measuredDataGetResponse.sdi12Data.size)
			Assertions.assertEquals(2, measuredDataGetResponse.environmentalData.size)
			Assertions.assertEquals(2, measuredDataGetResponse.voltageData.size)

			// アドレス1と2を両方取得できること
			Assertions.assertEquals(
				listOf("1", "2"),
				measuredDataGetResponse.sdi12Data.map { item -> item.sdiAddress }.distinct()
			)

			val sdi12ResponseList = listOf(
				Sdi12DataGetResponse(
					"1",
					mutableListOf(
						Sdi12DataAndDoy(
							1L,
							"1",
							"34.56",
							"12.34",
							"1.234",
							"5.67",
							"23.45",
							"2.345",
							"3.456",
							"4.567",
							LocalDateTime.parse("2025-01-01T01:00:00.000"),
							LocalDateTime.parse("2025-01-01T01:00:00.000"),
							null,
						),
						Sdi12DataAndDoy(
							2L,
							"1.1",
							"45.67",
							"23.45",
							"2.345",
							"6.789",
							"34.56",
							"3.456",
							"4.567",
							"5.678",
							LocalDateTime.parse("2025-01-01T02:00:00.000"),
							LocalDateTime.parse("2025-01-01T02:00:00.000"),
							null,
						)
					)
				),
				Sdi12DataGetResponse(
					"2",
					mutableListOf(
						Sdi12DataAndDoy(
							1L,
							"1",
							"34.56",
							"12.34",
							"1.234",
							"5.67",
							"23.45",
							"2.345",
							"3.456",
							"4.567",
							LocalDateTime.parse("2025-01-01T01:00:00.000"),
							LocalDateTime.parse("2025-01-01T01:00:00.000"),
							null,
						),
						Sdi12DataAndDoy(
							2L,
							"1.1",
							"45.67",
							"23.45",
							"2.345",
							"6.789",
							"34.56",
							"3.456",
							"4.567",
							"5.678",
							LocalDateTime.parse("2025-01-01T02:00:00.000"),
							LocalDateTime.parse("2025-01-01T02:00:00.000"),
							null,
						)
					)
				)
			)

			Assertions.assertEquals(
				sdi12ResponseList,
				measuredDataGetResponse.sdi12Data
			)

			val environmentalResponseList = listOf(
				EnvironmentalDataGetResponse(
					1L,
					"1",
					"1234.56",
					"23.45",
					"12.34",
					"1234",
					"345",
					"123",
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
					null
				),
				EnvironmentalDataGetResponse(
					2L,
					"1.1",
					"2345.67",
					"34.56",
					"23.45",
					"2345",
					"456",
					"234",
					LocalDateTime.parse("2025-01-01T02:00:00.000"),
					LocalDateTime.parse("2025-01-01T02:00:00.000"),
					null
				)
			)

			Assertions.assertEquals(
				environmentalResponseList,
				measuredDataGetResponse.environmentalData
			)

			val voltageDataGetResponse = mutableListOf(
				VoltageDataGetResponse(
					1L,
					"1",
					"12.34",
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
					LocalDateTime.parse("2025-01-01T01:00:00.000"),
					null
				),
				VoltageDataGetResponse(
					2L,
					"1.1",
					"23.45",
					LocalDateTime.parse("2025-01-01T02:00:00.000"),
					LocalDateTime.parse("2025-01-01T02:00:00.000"),
					null
				)
			)

			Assertions.assertEquals(
				voltageDataGetResponse,
				measuredDataGetResponse.voltageData
			)
		}

	}
}