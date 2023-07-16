package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.Sdi12Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "SDI-12データ")
public class Sdi12DataGetResponse {

    @Schema(description = "SDI-12アドレス", example = "1")
    private String sdiAddress;

    @Schema(description = "SDI-12測定データとDOYリスト")
    private List<Sdi12DataAndDoy> dataList;

    public static Sdi12DataAndDoy convertFromSdi12Data(Sdi12Data sdi12Data, Long measuredDataMasterId, String dayOfYear) {
        var sdi12DataAndDoy = new Sdi12DataAndDoy();
        sdi12DataAndDoy.setMeasuredDataMasterId(measuredDataMasterId);
        sdi12DataAndDoy.setDayOfYear(dayOfYear);
        sdi12DataAndDoy.setVwc(sdi12Data.getVwc());
        sdi12DataAndDoy.setSoilTemp(sdi12Data.getSoilTemp());
        sdi12DataAndDoy.setBrp(sdi12Data.getBrp());
        sdi12DataAndDoy.setSbec(sdi12Data.getSbec());
        sdi12DataAndDoy.setSpwec(sdi12Data.getSpwec());
        sdi12DataAndDoy.setGax(sdi12Data.getGax());
        sdi12DataAndDoy.setGay(sdi12Data.getGay());
        sdi12DataAndDoy.setGaz(sdi12Data.getGaz());
        sdi12DataAndDoy.setCreatedAt(sdi12Data.getMeasuredDataMaster().getCreatedAt());
        sdi12DataAndDoy.setUpdatedAt(sdi12Data.getMeasuredDataMaster().getUpdatedAt());
        sdi12DataAndDoy.setDeletedAt(sdi12Data.getMeasuredDataMaster().getDeletedAt());
        return sdi12DataAndDoy;
    }

    @Data
    @Schema(description = "SDI-12測定データとDOY")
    public static class Sdi12DataAndDoy {

        @Schema(description = "測定データマスターID", example = "1")
        private Long measuredDataMasterId;

        @Schema(description = "DOY", example = "111.11")
        private String dayOfYear;

        @Schema(description = "体積含水率", example = "11.11")
        private String vwc;

        @Schema(description = "地温", example = "11.11")
        private String soilTemp;

        @Schema(description = "比誘電率", example = "11.11")
        private String brp;

        @Schema(description = "電気伝導度", example = "11.11")
        private String sbec;

        @Schema(description = "間隙水電気伝導度", example = "11.11")
        private String spwec;

        @Schema(description = "重力加速度(X)", example = "1.11")
        private String gax;

        @Schema(description = "重力加速度(Y)", example = "1.11")
        private String gay;

        @Schema(description = "重力加速度(Z)", example = "1.11")
        private String gaz;

        @Schema(description = "作成日時", example = "2023-01-01T01:01:01.111111")
        private LocalDateTime createdAt;

        @Schema(description = "更新日時", example = "2023-01-01T01:01:01.111111")
        private LocalDateTime updatedAt;

        @Schema(description = "削除日時", example = "2023-01-01T01:01:01.111111")
        private LocalDateTime deletedAt;

    }
}
