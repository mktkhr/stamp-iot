package com.example.stamp_app.controller.microController;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.MicroControllerController;
import com.example.stamp_app.controller.param.MicroControllerPostParam;
import com.example.stamp_app.controller.response.MicroControllerPostResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MicroControllerController.class)
public class MicroControllerPostTest {

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

    private ResultActions mockMvcPerform(String requestBody) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(DummyData.MICROCONTROLLER_POST_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON));
    }

    private MicroControllerPostResponse generateMicroControllerResponse() {
        var microController = new MicroController(1L, DummyData.VALID_UUID, "モック", "AA:AA:AA:AA:AA:AA", "30", "1,3", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
        return new MicroControllerPostResponse(
                microController.getId(),
                microController.getUuid(),
                microController.getName(),
                microController.getMacAddress(),
                microController.getInterval(),
                microController.getSdi12Address(),
                microController.getCreatedAt(),
                microController.getUpdatedAt(),
                microController.getDeletedAt()
        );
    }

    @BeforeEach
    void setup() {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(microControllerService.addMicroControllerRelation(any(), any())).thenReturn(generateMicroControllerResponse());
    }

    @Nested
    @DisplayName("マイコン登録テスト")
    class MicroControllerRegisterTest {

        @Test
        void リクエストボディが正しいの値の場合400を返すこと() throws Exception {

            MicroControllerPostParam microControllerPostParam = new MicroControllerPostParam();
            microControllerPostParam.setUserId(1L);
            microControllerPostParam.setMacAddress(DummyData.VALID_MAC_ADDRESS);

            String requestBodyString = objectMapper.writeValueAsString(microControllerPostParam);
            mockMvcPerform(requestBodyString).andExpect(status().isOk());

        }

        @Nested
        @DisplayName("ユーザーID")
        class UserId {

            @Test
            void リクエストボディのユーザーIDがnullの場合400を返すこと() throws Exception {

                MicroControllerPostParam microControllerPostParam = new MicroControllerPostParam();
                microControllerPostParam.setMacAddress(DummyData.VALID_MAC_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(microControllerPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());

            }

            @Test
            void リクエストボディのユーザーIDが負の値の場合400を返すこと() throws Exception {

                MicroControllerPostParam microControllerPostParam = new MicroControllerPostParam();
                microControllerPostParam.setMacAddress(DummyData.VALID_MAC_ADDRESS);
                microControllerPostParam.setUserId(-1L);

                String requestBodyString = objectMapper.writeValueAsString(microControllerPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());

            }

            @Test
            void リクエストボディのユーザーIDがゼロの値の場合400を返すこと() throws Exception {

                MicroControllerPostParam microControllerPostParam = new MicroControllerPostParam();
                microControllerPostParam.setMacAddress(DummyData.VALID_MAC_ADDRESS);
                microControllerPostParam.setUserId(0L);

                String requestBodyString = objectMapper.writeValueAsString(microControllerPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());

            }

        }

        @Nested
        @DisplayName("MACアドレス")
        class MacAddress {

            @Test
            void リクエストボディのMACアドレスがnullの場合400を返すこと() throws Exception {

                MicroControllerPostParam microControllerPostParam = new MicroControllerPostParam();
                microControllerPostParam.setUserId(1L);

                String requestBodyString = objectMapper.writeValueAsString(microControllerPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());

            }

            @Test
            void リクエストボディのMACアドレスが不正な値の場合400を返すこと() throws Exception {

                MicroControllerPostParam microControllerPostParam = new MicroControllerPostParam();
                microControllerPostParam.setUserId(1L);
                microControllerPostParam.setMacAddress(DummyData.INVALID_MAC_ADDRESS);

                String requestBodyString = objectMapper.writeValueAsString(microControllerPostParam);
                mockMvcPerform(requestBodyString).andExpect(status().isBadRequest());

            }

        }

    }
}
