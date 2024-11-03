package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.MeasuredDataMaster;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Schema(description = "電圧データ")
public class VoltageDataGetResponse {

    @Schema(description = "測定データマスターID", example = "1")
    private Long measuredDataMasterId;

    @Schema(description = "DOY", example = "111.11")
    private String dayOfYear;

    @Schema(description = "電圧", example = "11.11")
    private String voltage;

    @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime createdAt;

    @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime updatedAt;

    @Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
    private LocalDateTime deletedAt;

    public static VoltageDataGetResponse convertFromMeasuredDataMaster(@NotNull final MeasuredDataMaster measuredDataMaster) {

        return new VoltageDataGetResponse(
                measuredDataMaster.getId(),
                measuredDataMaster.getDayOfYear(),
                measuredDataMaster.getVoltage(),
                measuredDataMaster.getCreatedAt(),
                measuredDataMaster.getUpdatedAt(),
                measuredDataMaster.getDeletedAt()
        );

    }

}
