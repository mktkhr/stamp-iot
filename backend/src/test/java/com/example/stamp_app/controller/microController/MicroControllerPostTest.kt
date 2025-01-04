package com.example.stamp_app.controller.microController

import com.example.stamp_app.common.interceptor.AppInterceptor
import com.example.stamp_app.controller.MicroControllerController
import com.example.stamp_app.controller.param.MicroControllerPostParam
import com.example.stamp_app.controller.response.MicroControllerPostResponse
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
class MicroControllerPostTest {
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

	private fun mockMvcPerform(requestBody: String): ResultActions {
		return mockMvc.perform(
			MockMvcRequestBuilders.post(DummyData.MICROCONTROLLER_POST_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.accept(MediaType.APPLICATION_JSON)
		)
	}

	private fun generateMicroControllerResponse(): MicroControllerPostResponse {
		val microController = MicroController(
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
		return MicroControllerPostResponse(
			microController.id!!,
			microController.uuid.toString(),
			microController.name,
			microController.macAddress,
			microController.interval,
			microController.sdi12Address,
			microController.createdAt!!,
			microController.updatedAt!!,
			null
		)
	}

	@BeforeEach
	fun setup() {
		whenever(appInterceptor.preHandle(any(), any(), any())).thenReturn(true)
		whenever(microControllerService.addMicroControllerRelation(any(), any()))
			.thenReturn(generateMicroControllerResponse())
	}

	@Nested
	@DisplayName("マイコン登録テスト")
	inner class MicroControllerRegisterTest {
		@Test
		fun リクエストボディが正しいの値の場合400を返すこと() {
			val microControllerPostParam = MicroControllerPostParam(
				1L,
				DummyData.VALID_MAC_ADDRESS
			)
			val requestBodyString = objectMapper.writeValueAsString(microControllerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isOk())
		}

		@Nested
		@DisplayName("ユーザーID")
		inner class UserId {

			@Test
			fun リクエストボディのユーザーIDが負の値の場合400を返すこと() {
				val microControllerPostParam = MicroControllerPostParam(
					-1L,
					DummyData.VALID_MAC_ADDRESS
				)
				val requestBodyString = objectMapper.writeValueAsString(microControllerPostParam)
				mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
			}

			@Test
			fun リクエストボディのユーザーIDがゼロの値の場合400を返すこと() {
				val microControllerPostParam = MicroControllerPostParam(
					0L,
					DummyData.VALID_MAC_ADDRESS
				)
				val requestBodyString = objectMapper.writeValueAsString(microControllerPostParam)
				mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
			}
		}

		@Nested
		@DisplayName("MACアドレス")
		inner class MacAddress {

			@Test
			fun リクエストボディのMACアドレスが不正な値の場合400を返すこと() {
				val microControllerPostParam = MicroControllerPostParam(
					1L,
					DummyData.INVALID_MAC_ADDRESS
				)
				val requestBodyString = objectMapper.writeValueAsString(microControllerPostParam)
				mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
			}
		}
	}
}
