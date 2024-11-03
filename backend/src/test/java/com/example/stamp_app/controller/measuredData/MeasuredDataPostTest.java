package com.example.stamp_app.controller.measuredData;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.MeasuredDataController;
import com.example.stamp_app.controller.param.EnvironmentalDataParam;
import com.example.stamp_app.controller.param.MeasuredDataPostParam;
import com.example.stamp_app.controller.param.Sdi12Param;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeasuredDataController.class)
public class MeasuredDataPostTest {

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

    private ResultActions mockMvcPerform(String requestBody) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(DummyData.MEASURED_DATA_POST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));
    }

    @BeforeEach
    void setup() {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Nested
    @DisplayName("測定結果登録テスト")
    class MeasuredDataInsertTest {

        private static final Sdi12Param VALID_SDI12_PARAM = new Sdi12Param(
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
        );

        private static final EnvironmentalDataParam VALID_ENVIRONMENTAL_PARAM = new EnvironmentalDataParam(
                "1013.1",
                "25.5",
                "56.6",
                "414",
                "3002",
                "787"
        );

        @Test
        void リクエストボディのマイコンのMACアドレスがnullの場合400を返すこと() throws Exception {
            MeasuredDataPostParam measuredDataPostParam = new MeasuredDataPostParam(
                    null,
                    List.of(VALID_SDI12_PARAM),
                    List.of(VALID_ENVIRONMENTAL_PARAM),
                    "11.11"
                    );

            String requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }

        @Test
        void リクエストボディのマイコンのMACアドレスが空白の場合400を返すこと() throws Exception {
            MeasuredDataPostParam measuredDataPostParam = new MeasuredDataPostParam(
                    "",
                    List.of(VALID_SDI12_PARAM),
                    List.of(VALID_ENVIRONMENTAL_PARAM),
                    "11.11"
            );

            String requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }

        @Test
        void リクエストボディのマイコンのMACアドレスが不適切なパターンの場合400を返すこと() throws Exception {
            MeasuredDataPostParam measuredDataPostParam = new MeasuredDataPostParam(
                    DummyData.INVALID_MAC_ADDRESS,
                    List.of(VALID_SDI12_PARAM),
                    List.of(VALID_ENVIRONMENTAL_PARAM),
                    "11.11"
            );

            String requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());
        }

        @Test
        void リクエストボディのマイコンのMACアドレスが適切な場合200を返すこと() throws Exception {
            MeasuredDataPostParam measuredDataPostParam = new MeasuredDataPostParam(
                    DummyData.VALID_MAC_ADDRESS,
                    List.of(VALID_SDI12_PARAM),
                    List.of(VALID_ENVIRONMENTAL_PARAM),
                    "11.11"
            );

            String requestBodyString = objectMapper.writeValueAsString(measuredDataPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isOk());
        }

    }
}
