package com.example.stamp_app.controller.measuredData;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.MeasuredDataController;
import com.example.stamp_app.entity.DummyData;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.MeasuredDataService;
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

@WebMvcTest(MeasuredDataController.class)
public class MeasuredDataGetTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    AppInterceptor appInterceptor;
    @MockBean
    RequestedUser requestedUser;
    @MockBean
    MeasuredDataService measuredDataService;
    @MockBean
    SessionService sessionService;
    @MockBean
    RedisService redisService;

    private ResultActions mockMvcPerform(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    void setup() {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Nested
    @DisplayName("測定結果取得テスト")
    class MeasuredDataSelectTest {

        @Test
        void リクエストパラメータのマイコンUUIDがnullの場合400を返すこと() throws Exception {

            mockMvcPerform(DummyData.MEASURED_DATA_GET_PATH).andExpect(status().isBadRequest());

        }

        @Test
        void リクエストパラメータのマイコンUUIDが空の場合400を返すこと() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get(DummyData.MEASURED_DATA_GET_PATH).param("microControllerUuid", "")).andExpect(status().isBadRequest());

        }

        @Test
        void リクエストパラメータのマイコンUUIDが不適切な値の場合400を返すこと() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get(DummyData.MEASURED_DATA_GET_PATH).param("microControllerUuid", DummyData.INVALID_UUID)).andExpect(status().isBadRequest());

        }

        @Test
        void リクエストパラメータのマイコンUUIDが適切な値の場合200を返すこと() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get(DummyData.MEASURED_DATA_GET_PATH).param("microControllerUuid", DummyData.VALID_UUID)).andExpect(status().isOk());

        }

    }
}
