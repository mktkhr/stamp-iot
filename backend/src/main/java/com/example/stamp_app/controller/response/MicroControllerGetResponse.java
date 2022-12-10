package com.example.stamp_app.controller.response;

import com.example.stamp_app.entity.MicroController;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MicroControllerGetResponse {

    private BigInteger id;

    private String name;

    private String macAddress;

    private int interval;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static List<MicroControllerGetResponse> convertMicroControllerToResponse(List<MicroController> microControllerList){
        List<MicroControllerGetResponse> microControllerGetResponseList = new ArrayList<>();

        microControllerList.forEach((microController) -> {
            var microControllerGetResponse = new MicroControllerGetResponse();
            microControllerGetResponse.setId(microController.getId());
            microControllerGetResponse.setName(microController.getName());
            microControllerGetResponse.setInterval(microController.getInterval());
            microControllerGetResponse.setMacAddress(microController.getMacAddress());
            microControllerGetResponse.setCreatedAt(microController.getCreatedAt());
            microControllerGetResponse.setUpdatedAt(microController.getCreatedAt());
            microControllerGetResponseList.add(microControllerGetResponse);
        });

        return microControllerGetResponseList;
    }

}
