package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MicroControllerPostParam;
import com.example.stamp_app.controller.response.MicroControllerGetResponse;
import com.example.stamp_app.controller.response.MicroControllerPostResponse;
import com.example.stamp_app.service.AccountService;
import com.example.stamp_app.service.MicroControllerService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/ems/micro-controller")
public class MicroControllerController {
    @Autowired
    MicroControllerService microControllerService;
    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;

    /**
     * マイコン登録API
     */
    @PostMapping
    public ResponseEntity<String> addMicroControllerRelation(@RequestBody MicroControllerPostParam microControllerPostParam) {
        System.out.println(">> MicroController Controller(micro-controller:POST)");
        System.out.println("RequestBody:" + microControllerPostParam);

        MicroControllerPostResponse microControllerPostResponse = microControllerService.addMicroControllerRelation(microControllerPostParam.getUserId(), microControllerPostParam.getMacAddress());

        System.out.println("<< MicroController Controller(micro-controller:POST)");
        return new ResponseEntity<>(microControllerPostResponse.getMicroController().getMacAddress(), microControllerPostResponse.getStatus());
    }

    /**
     * アカウントに紐づくマイコン一覧取得API
     */
    @GetMapping(value = "/info")
    public ResponseEntity<List<MicroControllerGetResponse>> getMicroControllerInfo(HttpServletRequest httpServletRequest) {
        System.out.println(">> MicroController Controller(micro-controller:GET)");
        var cookieLIst = httpServletRequest.getCookies();

        var sessionUuid = sessionService.getSessionUuidFromCookie(cookieLIst);
        if(sessionUuid == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var userUuid = redisService.getUserUuidFromSessionUuid(sessionUuid);
        if(userUuid == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var microControllerList = microControllerService.getMicroControllerList(userUuid);

        System.out.println("<< MicroController Controller(micro-controller:GET)");
        return new ResponseEntity<>(microControllerList, HttpStatus.OK);
    }
}
