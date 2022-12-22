package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MeasuredDataPostParam;
import com.example.stamp_app.controller.response.measuredDataGetResponse.MeasuredDataGetResponse;
import com.example.stamp_app.service.MeasuredDataService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping(value = "/ems/measured-data")
public class MeasuredDataController {

    @Autowired
    MeasuredDataService measuredDataService;
    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;

    /**
     * 測定データ登録API
     *
     * @param measuredDataPostParam 測定データ
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<HttpStatus> addMeasuredData(@RequestBody MeasuredDataPostParam measuredDataPostParam) {
        System.out.println(">> Measured Data Controller(POST)");
        System.out.println("RequestBody:" + measuredDataPostParam);

        var response = measuredDataService.addMeasuredData(measuredDataPostParam);

        System.out.println("<< Measured Data Controller(POST)");
        return new ResponseEntity<>(response);
    }

    /**
     * アカウントIDとマイコンIDを指定して測定結果を取得するAPI
     *
     * @param microControllerId マイコンID
     * @return 測定結果
     */
    @GetMapping
    public ResponseEntity<MeasuredDataGetResponse> getMeasuredData(@RequestParam BigInteger microControllerId, HttpServletRequest httpServletRequest) {
        System.out.println(">> Measured Data Controller(POST)");
        System.out.println("RequestParam: microControllerId:" + microControllerId);

        var cookieLIst = httpServletRequest.getCookies();

        var sessionUuid = sessionService.getSessionUuidFromCookie(cookieLIst);

        var userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);

        var response = measuredDataService.getMeasuredData(userUuid, microControllerId);

        System.out.println("<< Measured Data Controller(POST)");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
