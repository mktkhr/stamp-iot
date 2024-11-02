package com.example.stamp_app.controller.response;

import com.example.stamp_app.entity.MicroController;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "マイコン詳細データ")
@AllArgsConstructor
public class MicroControllerGetResponse {

    @Schema(description = "マイコンID", example = "1")
    private final Long id;

    @Schema(description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
    private final String uuid;

    @Schema(description = "マイコン名", example = "サンプル端末")
    private final String name;

    @Schema(description = "MACアドレス", example = "AA:AA:AA:AA:AA:AA")
    private final String macAddress;

    @Schema(description = "測定間隔", example = "60")
    private final String interval;

    @Schema(description = "SDI-12アドレス", example = "1,3")
    private final String sdi12Address;

    @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
    private final LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
    private final LocalDateTime updatedAt;

    public static List<MicroControllerGetResponse> convertMicroControllerToListResponse(
            List<MicroController> microControllerList) {
        List<MicroControllerGetResponse> microControllerGetResponseList = new ArrayList<>();

        microControllerList.forEach((microController) -> {
            var microControllerGetResponse = new MicroControllerGetResponse(
                    microController.getId(),
                    microController.getUuid().toString(),
                    microController.getName(),
                    microController.getMacAddress(),
                    microController.getInterval(),
                    microController.getSdi12Address(),
                    microController.getCreatedAt(),
                    microController.getUpdatedAt());
            microControllerGetResponseList.add(microControllerGetResponse);
        });

        return microControllerGetResponseList;
    }

    public static MicroControllerGetResponse convertMicroControllerToDetailResponse(MicroController microController) {

        return new MicroControllerGetResponse(
                microController.getId(),
                microController.getUuid().toString(),
                microController.getName(),
                microController.getMacAddress(),
                microController.getInterval(),
                microController.getSdi12Address(),
                microController.getCreatedAt(),
                microController.getUpdatedAt());
    }

}
