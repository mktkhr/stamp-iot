package com.example.stamp_app.controller.account;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.AccountController;
import com.example.stamp_app.controller.param.account.LoginPostParam;
import com.example.stamp_app.controller.response.AccountLoginResponse;
import com.example.stamp_app.entity.Account;
import com.example.stamp_app.entity.DummyData;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.AccountService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class LoginPostTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AppInterceptor appInterceptor;
    @MockBean
    RequestedUser requestedUser;
    @MockBean
    AccountService accountService;
    @MockBean
    SessionService sessionService;
    @MockBean
    RedisService redisService;

    private ResultActions mockMvcPerform(String requestBody) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(DummyData.LOGIN_POST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    void setup() throws IllegalAccessException {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        var account = new Account();
        account.setUuid(UUID.randomUUID());
        when(accountService.login(any())).thenReturn(new AccountLoginResponse(HttpStatus.OK, account));
        when(sessionService.generateCookie(any())).thenReturn(new Cookie("test", "cookie"));
    }

    @Nested
    @DisplayName("ログインテスト")
    class AccountLoginTest {


        @Nested
        @DisplayName("メールアドレステスト")
        class MailAddressTest {
            @Test
            void リクエストボディのアカウントのメールアドレスが不適切な場合400を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.VALID_8_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.INVALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("パスワードテスト(nullチェックのみ)")
        class PasswordTest {

            @Test
            void リクエストボディのアカウントのパスワードがnullの場合400を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
            }

            @Test
            void リクエストボディのアカウントのパスワードが7文字の場合200を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.INVALID_7_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isOk());
            }

            @Test
            void リクエストボディのアカウントのパスワードが8文字の場合200を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.VALID_8_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isOk());
            }

            @Test
            void リクエストボディのアカウントのパスワードが9文字の場合200を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.VALID_9_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isOk());
            }

            @Test
            void リクエストボディのアカウントのパスワードが23文字の場合200を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.VALID_23_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isOk());
            }

            @Test
            void リクエストボディのアカウントのパスワードが24文字の場合200を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.VALID_24_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isOk());
            }

            @Test
            void リクエストボディのアカウントのパスワードが25文字の場合200を返すこと() throws Exception {
                LoginPostParam loginPostParam = new LoginPostParam();
                loginPostParam.setPassword(DummyData.INVALID_25_LENGTH_PASSWORD);
                loginPostParam.setEmail(DummyData.VALID_EMAIL_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(loginPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isOk());
            }
        }
    }
}
