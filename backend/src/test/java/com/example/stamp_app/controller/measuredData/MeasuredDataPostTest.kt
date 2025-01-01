package com.example.stamp_app.controller.measuredData

import com.example.stamp_app.common.interceptor.AppInterceptor
import com.example.stamp_app.controller.MeasuredDataController
import com.example.stamp_app.controller.param.EnvironmentalDataParam
import com.example.stamp_app.controller.param.MeasuredDataPostParam
import com.example.stamp_app.controller.param.Sdi12Param
import com.example.stamp_app.entity.DummyData
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.MeasuredDataService
import com.example.stamp_app.session.RedisService
import com.example.stamp_app.session.SessionService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.List

@WebMvcTest(MeasuredDataController::class)
class MeasuredDataPostTest {
	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@MockBean
	private lateinit var appInterceptor: AppInterceptor

	@MockBean
	private lateinit var requestedUser: RequestedUser

	@MockBean
	private lateinit var measuredDataService: MeasuredDataService

	@MockBean
	private lateinit var sessionService: SessionService

	@MockBean
	private lateinit var redisService: RedisService

	private val validSdi12Param = Sdi12Param(
		1L,
		"1",
		"11.11",
		"22.22",
		"33.33",
		"44.44",
		"55.55",
		"66.66",
		"77.77",
		"88.88"
	)
	private val VALID_ENVIRONMENTAL_PARAM = EnvironmentalDataParam(
		"1013.1",
		"25.5",
		"56.6",
		"414",
		"3002",
		"787"
	)

	@Throws(Exception::class)
	private fun mockMvcPerform(requestBody: String): ResultActions {
		return mockMvc.perform(
			MockMvcRequestBuilders.post(DummyData.MEASURED_DATA_POST_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		)
	}

	@BeforeEach
	fun setup() {
		whenever(
			appInterceptor.preHandle(
				any(),
				any(),
				any()
			)
		).thenReturn(true)
	}

	@Nested
	@DisplayName("測定結果登録テスト")
	internal inner class MeasuredDataInsertTest {

		@Test
		@Throws(Exception::class)
		fun リクエストボディのマイコンのMACアドレスが空白の場合400を返すこと() {
			val measuredDataPostParam = MeasuredDataPostParam(
				"",
				List.of(validSdi12Param),
				List.of(VALID_ENVIRONMENTAL_PARAM),
				"11.11"
			)
			val requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのマイコンのMACアドレスが不適切なパターンの場合400を返すこと() {
			val measuredDataPostParam = MeasuredDataPostParam(
				DummyData.INVALID_MAC_ADDRESS,
				List.of(validSdi12Param),
				List.of(VALID_ENVIRONMENTAL_PARAM),
				"11.11"
			)
			val requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのマイコンのMACアドレスが適切な場合200を返すこと() {
			val measuredDataPostParam = MeasuredDataPostParam(
				DummyData.VALID_MAC_ADDRESS,
				List.of(validSdi12Param),
				List.of(VALID_ENVIRONMENTAL_PARAM),
				"11.11"
			)
			val requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isOk())
		}
	}
}
