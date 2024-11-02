package com.example.stamp_app.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "マイコンデータ")
@AllArgsConstructor
public class MicroControllerPostResponse {

    @Schema(description = "マイコンID", example = "1")
    private Long id;

    @Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
    private String uuid;

    @Schema(description = "マイコン名", example = "サンプル端末")
    private String name;

    @Schema(description = "MACアドレス", example = "AA:AA:AA:AA:AA:AA")
    private String macAddress;

    @Schema(description = "測定間隔", example = "60")
    private String interval;

    @Schema(description = "SDI-12アドレス", example = "1,3")
    private String sdi12Address;

    @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime updatedAt;

    @Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime deletedAt;
}
