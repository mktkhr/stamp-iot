package com.example.stamp_app.controller.account;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.AccountController;
import com.example.stamp_app.controller.param.account.RegisterPostParam;
import com.example.stamp_app.entity.DummyData;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.AccountService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class RegisterPostTest {

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
        return mockMvc.perform(MockMvcRequestBuilders.post(DummyData.REGISTER_POST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    void setup() {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Nested
    @DisplayName("アカウント登録テスト")
    class AddAccountTest {

        @Test
        void リクエストボディのアカウントが空の場合400を返すこと() throws Exception {
            String requestBodyString = "";
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }

        @Test
        void リクエストボディのアカウントのメールアドレスが不適切な場合400を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.INVALID_EMAIL_ADDRESS,
                            DummyData.VALID_8_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }

        @Test
        void リクエストボディのアカウントのパスワード7文字の場合400を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.VALID_EMAIL_ADDRESS,
                            DummyData.INVALID_7_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }

        @Test
        void リクエストボディのアカウントのパスワード8文字の場合200を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.VALID_EMAIL_ADDRESS,
                            DummyData.VALID_8_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isOk());
        }

        @Test
        void リクエストボディのアカウントのパスワード9文字の場合200を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.VALID_EMAIL_ADDRESS,
                            DummyData.VALID_9_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isOk());
        }

        @Test
        void リクエストボディのアカウントのパスワード23文字の場合200を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.VALID_EMAIL_ADDRESS,
                            DummyData.VALID_23_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isOk());
        }

        @Test
        void リクエストボディのアカウントのパスワード24文字の場合200を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.VALID_EMAIL_ADDRESS,
                            DummyData.VALID_24_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isOk());
        }

        @Test
        void リクエストボディのアカウントのパスワード25文字の場合400を返すこと() throws Exception {
            final var registerPostParam =
                    new RegisterPostParam(
                            DummyData.VALID_EMAIL_ADDRESS,
                            DummyData.INVALID_25_LENGTH_PASSWORD
                    );

            final var requestBodyString = objectMapper.writeValueAsString(registerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }
    }
}
