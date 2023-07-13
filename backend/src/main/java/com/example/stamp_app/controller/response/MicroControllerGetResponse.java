package com.example.stamp_app.controller.response;

import com.example.stamp_app.entity.MicroController;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MicroControllerGetResponse {

    private Long id;

    private String uuid;

    private String name;

    private String macAddress;

    private String interval;

    private String sdi12Address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static List<MicroControllerGetResponse> convertMicroControllerToListResponse(List<MicroController> microControllerList) {
        List<MicroControllerGetResponse> microControllerGetResponseList = new ArrayList<>();

        microControllerList.forEach((microController) -> {
            var microControllerGetResponse = new MicroControllerGetResponse();
            microControllerGetResponse.setId(microController.getId());
            microControllerGetResponse.setUuid(microController.getUuid());
            microControllerGetResponse.setName(microController.getName());
            microControllerGetResponse.setInterval(microController.getInterval());
            microControllerGetResponse.setSdi12Address(microController.getSdi12Address());
            microControllerGetResponse.setMacAddress(microController.getMacAddress());
            microControllerGetResponse.setCreatedAt(microController.getCreatedAt());
            microControllerGetResponse.setUpdatedAt(microController.getUpdatedAt());
            microControllerGetResponseList.add(microControllerGetResponse);
        });

        return microControllerGetResponseList;
    }

    public static MicroControllerGetResponse convertMicroControllerToDetailResponse(MicroController microController) {

        var microControllerGetResponse = new MicroControllerGetResponse();
        microControllerGetResponse.setId(microController.getId());
        microControllerGetResponse.setUuid(microController.getUuid());
        microControllerGetResponse.setName(microController.getName());
        microControllerGetResponse.setInterval(microController.getInterval());
        microControllerGetResponse.setSdi12Address(microController.getSdi12Address());
        microControllerGetResponse.setMacAddress(microController.getMacAddress());
        microControllerGetResponse.setCreatedAt(microController.getCreatedAt());
        microControllerGetResponse.setUpdatedAt(microController.getUpdatedAt());

        return microControllerGetResponse;
    }

}
