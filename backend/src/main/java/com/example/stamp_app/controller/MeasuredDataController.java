package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MeasuredDataPostParam;
import com.example.stamp_app.controller.response.measuredDataGetResponse.MeasuredDataGetResponse;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.MeasuredDataService;
import com.example.stamp_app.session.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/ems/measured-data")
public class MeasuredDataController {

    @Autowired
    MeasuredDataService measuredDataService;
    @Autowired
    RedisService redisService;
    @Autowired
    RequestedUser requestedUser;

    /**
     * 測定データ登録API
     *
     * @param measuredDataPostParam 測定データ
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<HttpStatus> addMeasuredData(@RequestBody @Validated MeasuredDataPostParam measuredDataPostParam) {

        measuredDataService.addMeasuredData(measuredDataPostParam);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * アカウントIDとマイコンIDを指定して測定結果を取得するAPI
     *
     * @param microControllerUuid マイコンID
     * @return 測定結果
     */
    @GetMapping
    public ResponseEntity<MeasuredDataGetResponse> getMeasuredData(@RequestParam @Validated String microControllerUuid) {

        var userUuid = redisService.getUserUuidFromSessionUuid(requestedUser.getSessionUuid());

        var response = measuredDataService.getMeasuredData(userUuid, microControllerUuid);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
