package com.example.stamp_app.controller.microController

import com.example.stamp_app.common.interceptor.AppInterceptor
import com.example.stamp_app.controller.MicroControllerController
import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam
import com.example.stamp_app.entity.DummyData
import com.example.stamp_app.entity.MicroController
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.AccountService
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
class MicroControllerPatchTest {
	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var objectMapper: ObjectMapper

	@MockBean
	private lateinit var appInterceptor: AppInterceptor

	@MockBean
	private lateinit var requestedUser: RequestedUser

	@MockBean
	private lateinit var accountService: AccountService

	@MockBean
	private lateinit var microControllerService: MicroControllerService

	@MockBean
	private lateinit var sessionService: SessionService

	@MockBean
	private lateinit var redisService: RedisService

	private val mockController = MicroController(
		id = 1L,
		uuid = UUID.randomUUID(),
		name = "モック端末",
		macAddress = "AA:BB:CC:DD:EE:FF",
		interval = "30",
		sdi12Address = "1,3",
		createdAt = LocalDateTime.now(),
		updatedAt = LocalDateTime.now(),
		deletedAt = null,
		measuredDataMasters = emptyList(),
		account = null
	)

	@BeforeEach
	fun setup() {

		whenever(requestedUser.userUuid).thenReturn("")

		whenever(
			microControllerService.updateMicroControllerDetail(
				any<String>(),
				any<MicroControllerPatchParam>()
			)
		).thenReturn(mockController)

		whenever(
			appInterceptor.preHandle(any(), any(), any())
		).thenReturn(true)

	}

	private fun mockMvcPerform(param: String): ResultActions {
		return mockMvc.perform(
			MockMvcRequestBuilders.patch(DummyData.MICROCONTROLLER_PATCH_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(param)
		)
	}

	@Test
	fun リクエストパラメータが足りている場合200を返すこと() {
		whenever(microControllerService.updateMicroControllerDetail(any(), any()))
			.thenReturn(
				mockController
			)

		val microControllerPatchParam = MicroControllerPatchParam(
			UUID.randomUUID(),
			"HOGE",
			"1",
			"1,3"
		)
		val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
		mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
	}

	@Nested
	@DisplayName("マイコン更新テスト")
	inner class PatchTest {

		@Nested
		@DisplayName("名前のテスト")
		inner class MicroControllerName {
			@Test
			@Throws(Exception::class)
			fun リクエストパラメータのマイコン名が空の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "", "1", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}
		}

		@Nested
		@DisplayName("測定間隔のテスト")
		inner class MicroControllerInterval {

			@Test
			fun リクエストパラメータの測定間隔が不正の場合400を返すこと() {
				val microControllerPatchParam =
					MicroControllerPatchParam(UUID.randomUUID(), "HOGE", DummyData.INVALID_INTERVAL, "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isBadRequest())
			}

			@Test
			fun リクエストパラメータの測定間隔が1の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータの測定間隔が5の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "5", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータの測定間隔が10の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "10", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータの測定間隔が15の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "15", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータの測定間隔が20の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "20", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータの測定間隔が30の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "30", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータの測定間隔が60の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "60", "1,3")
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}
		}

		@Nested
		@DisplayName("SDI12アドレスのテスト")
		inner class MicroControllerSDI12Address {
			@Test
			fun リクエストパラメータのSDI12アドレスが空の場合200を返すこと() {
				val microControllerPatchParam = MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", null)
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isOk())
			}

			@Test
			fun リクエストパラメータのSDi12アドレスが不正の場合200を返すこと() {
				val microControllerPatchParam =
					MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", DummyData.INVALID_SDI_ADDRESS)
				val jsonString = objectMapper.writeValueAsString(microControllerPatchParam)
				mockMvcPerform(jsonString).andExpect(MockMvcResultMatchers.status().isBadRequest())
			}
		}
	}
}
