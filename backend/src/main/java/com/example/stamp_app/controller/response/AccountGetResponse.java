package com.example.stamp_app.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "アカウント情報")
public class AccountGetResponse {

    @Schema(description = "アカウントID", example = "1")
    private Long id;

    @Schema(description = "登録名", example = "サンプル太郎")
    private String name;

    @Schema(description = "作成日", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime createdAt;

    @Schema(description = "更新日", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime updatedAt;

}
