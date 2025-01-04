package com.example.stamp_app.controller.account

import com.example.stamp_app.common.interceptor.AppInterceptor
import com.example.stamp_app.controller.AccountController
import com.example.stamp_app.controller.param.account.RegisterPostParam
import com.example.stamp_app.entity.DummyData
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.AccountService
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

@WebMvcTest(AccountController::class)
class RegisterPostTest {
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
	private lateinit var sessionService: SessionService

	@MockBean
	private lateinit var redisService: RedisService

	@Throws(Exception::class)
	private fun mockMvcPerform(requestBody: String): ResultActions {
		return mockMvc.perform(
			MockMvcRequestBuilders.post(DummyData.REGISTER_POST_PATH)
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
	@DisplayName("アカウント登録テスト")
	internal inner class AddAccountTest {

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのメールアドレスが不適切な場合400を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.INVALID_EMAIL_ADDRESS,
				DummyData.VALID_8_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのパスワード7文字の場合400を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.VALID_EMAIL_ADDRESS,
				DummyData.INVALID_7_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのパスワード8文字の場合200を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.VALID_EMAIL_ADDRESS,
				DummyData.VALID_8_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isOk())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのパスワード9文字の場合200を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.VALID_EMAIL_ADDRESS,
				DummyData.VALID_9_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isOk())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのパスワード23文字の場合200を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.VALID_EMAIL_ADDRESS,
				DummyData.VALID_23_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isOk())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのパスワード24文字の場合200を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.VALID_EMAIL_ADDRESS,
				DummyData.VALID_24_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isOk())
		}

		@Test
		@Throws(Exception::class)
		fun リクエストボディのアカウントのパスワード25文字の場合400を返すこと() {
			val registerPostParam = RegisterPostParam(
				DummyData.VALID_EMAIL_ADDRESS,
				DummyData.INVALID_25_LENGTH_PASSWORD
			)
			val requestBodyString = objectMapper.writeValueAsString(registerPostParam)
			mockMvcPerform(requestBodyString).andExpect(MockMvcResultMatchers.status().isBadRequest())
		}
	}
}
