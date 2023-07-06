package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MicroControllerPostParam;
import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam;
import com.example.stamp_app.controller.response.MicroControllerGetResponse;
import com.example.stamp_app.controller.response.MicroControllerPostResponse;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.MicroControllerService;
import com.example.stamp_app.session.RedisService;
import com.example.stamp_app.session.SessionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping(value = "/ems/micro-controller")
public class MicroControllerController {
    @Autowired
    MicroControllerService microControllerService;
    @Autowired
    SessionService sessionService;
    @Autowired
    RedisService redisService;
    @Autowired
    RequestedUser requestedUser;

    /**
     * マイコン登録API
     */
    @PostMapping
    public ResponseEntity<String> addMicroControllerRelation(@RequestBody MicroControllerPostParam microControllerPostParam) {

        MicroControllerPostResponse microControllerPostResponse = microControllerService.addMicroControllerRelation(microControllerPostParam.getUserId(), microControllerPostParam.getMacAddress());

        return new ResponseEntity<>(microControllerPostResponse.getMicroController().getMacAddress(), microControllerPostResponse.getStatus());
    }

    /**
     * アカウントに紐づくマイコン一覧取得API
     */
    @GetMapping(value = "/info")
    public ResponseEntity<List<MicroControllerGetResponse>> getMicroControllerInfo() {

        var microControllerList = microControllerService.getMicroControllerList(requestedUser.getUserUuid());

        return new ResponseEntity<>(microControllerList, HttpStatus.OK);
    }

    /**
     * マイコン詳細情報取得
     *
     * @param microControllerUuid マイコンUUID
     */

    @GetMapping(value = "/detail")
    public ResponseEntity<MicroControllerGetResponse> getMicroControllerDetail(@RequestParam String microControllerUuid) {
        var microController = microControllerService.getMicroControllerDetail(microControllerUuid);
        var response = MicroControllerGetResponse.convertMicroControllerToDetailResponse(microController);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * マイコン詳細更新API
     *
     * @param param 更新パラメータ
     */
    @PatchMapping(value = "/detail")
    public ResponseEntity<MicroControllerGetResponse> updateMicroControllerDetail(@Valid @Validated @RequestBody MicroControllerPatchParam param) {

        var microController = microControllerService.updateMicroControllerDetail(requestedUser.getUserUuid(), param);
        var response = MicroControllerGetResponse.convertMicroControllerToDetailResponse(microController);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
