package com.example.stamp_app.controller.response.microController;

import com.example.stamp_app.entity.MicroController;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "マイコン詳細データ(セッション無し)")
@Getter
@AllArgsConstructor
public class MicroControllerGetDetailNoSessionResponse {

    @Schema(description = "測定間隔", example = "60")
    private String interval;

    @Schema(description = "SDI-12アドレス", example = "1,3")
    private String sdi12Address;

    public static MicroControllerGetDetailNoSessionResponse convertMicroControllerToDetailResponse(MicroController microController) {

        return new MicroControllerGetDetailNoSessionResponse(
                microController.getInterval(),
                microController.getSdi12Address()
        );
    }
}
