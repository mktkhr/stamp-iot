package com.example.stamp_app.controller.response.measuredDataGetResponse;

import com.example.stamp_app.entity.Sdi12Data;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Sdi12DataGetResponse {

    private String sdiAddress;

    private List<Sdi12DataAndDoy> dataList;

    @Data
    public static class Sdi12DataAndDoy {

        private BigInteger measuredDataMasterId;

        private String dayOfYear;

        private String vwc;

        private String soilTemp;

        private String brp;

        private String sbec;

        private String spwec;

        private String gax;

        private String gay;

        private String gaz;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        private LocalDateTime deletedAt;

    }

    public static Sdi12DataAndDoy convertFromSdi12Data(Sdi12Data sdi12Data, BigInteger measuredDataMasterId, String dayOfYear){
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
}
