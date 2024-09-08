package com.example.stamp_app.controller.microController;

import com.example.stamp_app.common.interceptor.AppInterceptor;
import com.example.stamp_app.controller.MicroControllerController;
import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam;
import com.example.stamp_app.entity.DummyData;
import com.example.stamp_app.entity.MicroController;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.AccountService;
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
public class MicroControllerPatchTest {

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
    MicroControllerService microControllerService;
    @MockBean
    SessionService sessionService;
    @MockBean
    RedisService redisService;

    private MicroController generateMicroController() {
        return new MicroController(1L, UUID.randomUUID(), "モック", "AA:AA:AA:AA:AA:AA", "30", "1,3", LocalDateTime.now(), LocalDateTime.now(), null, null, null);
    }

    @BeforeEach
    void setup() {
        when(appInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(microControllerService.updateMicroControllerDetail(any(), any())).thenReturn(generateMicroController());
    }


    @Nested
    @DisplayName("マイコン更新テスト")
    class PatchTest {

        private ResultActions mockMvcPerform(String param) throws Exception {
            return mockMvc.perform(MockMvcRequestBuilders.patch(DummyData.MICROCONTROLLER_PATCH_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.valueOf(param))
                    .accept(MediaType.APPLICATION_JSON));
        }

        @Test
        void リクエストパラメータが足りている場合200を返すこと() throws Exception {
            MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", "1,3");
            var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
            mockMvcPerform(jsonString).andExpect(status().isOk());
        }


        @Nested
        @DisplayName("UUIDのテスト")
        class MicroControllerUuid {

            @Test
            void リクエストパラメータのマイコンUUIDが空の場合400を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(null, "HOGE", "30", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isBadRequest());
            }

        }

        @Nested
        @DisplayName("名前のテスト")
        class MicroControllerName {

            @Test
            void リクエストパラメータのマイコン名が空の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), null, "1", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

        }

        @Nested
        @DisplayName("測定間隔のテスト")
        class MicroControllerInterval {

            @Test
            void リクエストパラメータの測定間隔が空の場合400を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", null, "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isBadRequest());
            }

            @Test
            void リクエストパラメータの測定間隔が不正の場合400を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", DummyData.INVALID_INTERVAL, "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isBadRequest());
            }

            @Test
            void リクエストパラメータの測定間隔が1の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータの測定間隔が5の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "5", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータの測定間隔が10の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "10", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータの測定間隔が15の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "15", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータの測定間隔が20の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "20", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータの測定間隔が30の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "30", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータの測定間隔が60の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "60", "1,3");
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

        }

        @Nested
        @DisplayName("SDI12アドレスのテスト")
        class MicroControllerSDI12Address {

            @Test
            void リクエストパラメータのSDI12アドレスが空の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", null);
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isOk());
            }

            @Test
            void リクエストパラメータのSDi12アドレスが不正の場合200を返すこと() throws Exception {
                MicroControllerPatchParam microControllerPatchParam = new MicroControllerPatchParam(UUID.randomUUID(), "HOGE", "1", DummyData.INVALID_SDI_ADDRESS);
                var jsonString = objectMapper.writeValueAsString(microControllerPatchParam);
                mockMvcPerform(jsonString).andExpect(status().isBadRequest());
            }

        }
    }
}
