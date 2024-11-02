package com.example.stamp_app.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "アカウント情報")
public record AccountGetResponse(
        @Schema(description = "アカウントID", example = "1") Long id,
        @Schema(description = "登録名", example = "サンプル太郎") String name,
        @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111") LocalDateTime createdAt,
        @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111") LocalDateTime updatedAt) {

}
