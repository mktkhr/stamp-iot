package com.example.stamp_app.controller.microController

import com.example.stamp_app.common.interceptor.AppInterceptor
import com.example.stamp_app.controller.MicroControllerController
import com.example.stamp_app.entity.DummyData
import com.example.stamp_app.entity.MicroController
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.MicroControllerService
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
import java.time.LocalDateTime
import java.util.*

@WebMvcTest(MicroControllerController::class)
class MicroControllerGetTest {
	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@MockBean
	private lateinit var appInterceptor: AppInterceptor

	@MockBean
	private lateinit var requestedUser: RequestedUser

	@MockBean
	private lateinit var microControllerService: MicroControllerService

	@MockBean
	private lateinit var sessionService: SessionService

	@MockBean
	private lateinit var redisService: RedisService

	private fun generateMicroController(): MicroController {
		return MicroController(
			1L,
			UUID.randomUUID(),
			"モック",
			"AA:AA:AA:AA:AA:AA",
			"30",
			"1,3",
			LocalDateTime.now(),
			LocalDateTime.now(),
			null,
			null,
			null
		)
	}

	@Throws(Exception::class)
	private fun mockMvcPerform(): ResultActions {
		return mockMvc.perform(
			MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_GET_PATH)
				.contentType(MediaType.APPLICATION_JSON)
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

		whenever(microControllerService.getMicroControllerDetail(any()))
			.thenReturn(generateMicroController())
	}

	@Nested
	@DisplayName("マイコン詳細取得テスト")
	internal inner class GetTest {
		@Test
		@Throws(Exception::class)
		fun リクエストパラメータのマイコンUUIDがnullの場合400を返すこと() {
			mockMvcPerform().andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストパラメータのマイコンUUIDが空の場合400を返すこと() {
			mockMvc.perform(
				MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_GET_PATH).param("microControllerUuid", "")
			).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストパラメータのマイコンUUIDが不適切な値の場合400を返すこと() {
			mockMvc.perform(
				MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_GET_PATH)
					.param("microControllerUuid", DummyData.INVALID_UUID)
			).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストパラメータのマイコンUUIDが適切な値の場合200を返すこと() {
			mockMvc.perform(
				MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_GET_PATH)
					.param("microControllerUuid", DummyData.VALID_UUID)
			).andExpect(MockMvcResultMatchers.status().isOk())
		}
	}
}
