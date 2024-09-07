package com.example.stamp_app.controller.microController;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.MicroControllerController;
import com.example.stamp_app.entity.DummyData;
import com.example.stamp_app.entity.MicroController;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.MicroControllerService;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MicroControllerController.class)
public class MicroControllerNoSessionGetTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AppInterceptor appInterceptor;
    @MockBean
    RequestedUser requestedUser;
    @MockBean
    MicroControllerService microControllerService;
    @MockBean
    SessionService sessionService;
    @MockBean
    RedisService redisService;

    private MicroController generateMicroController() {
        return new MicroController(1L, UUID.randomUUID(), "モック", "AA:AA:AA:AA:AA:AA", "30", "1,3", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
    }

    private ResultActions mockMvcPerform(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    void setup() {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(microControllerService.getMicroControllerDetailWithMacAddress(any())).thenReturn(generateMicroController());
    }

    @Nested
    @DisplayName("マイコン詳細取得テスト")
    class GetTest {

        @Test
        void リクエストパラメータのMACアドレスがnullの場合400を返すこと() throws Exception {

            mockMvcPerform(DummyData.MICROCONTROLLER_NO_SESSION_GET_PATH).andExpect(status().isBadRequest());

        }

        @Test
        void リクエストパラメータのMACアドレスが空の場合400を返すこと() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_NO_SESSION_GET_PATH).param("macAddress", "")).andExpect(status().isBadRequest());

        }

        @Test
        void リクエストパラメータのMACアドレスが不適切な値の場合400を返すこと() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_NO_SESSION_GET_PATH).param("macAddress", DummyData.INVALID_MAC_ADDRESS)).andExpect(status().isBadRequest());

        }

        @Test
        void リクエストパラメータのMACアドレスが適切な値の場合200を返すこと() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get(DummyData.MICROCONTROLLER_NO_SESSION_GET_PATH).param("macAddress", DummyData.VALID_MAC_ADDRESS)).andExpect(status().isOk());

        }
    }
}
