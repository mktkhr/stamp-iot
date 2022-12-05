package com.example.stamp_app.controller;

import com.example.stamp_app.controller.Param.MicroControllerPostParam;
import com.example.stamp_app.controller.Response.MicroControllerPostResponse;
import com.example.stamp_app.service.MicroControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/ems/micro-controller")
public class MicroControllerController {
    @Autowired
    MicroControllerService microControllerService;

    @PostMapping
    public ResponseEntity<String> addMicroControllerRelation(@RequestBody MicroControllerPostParam microControllerPostParam) {
        System.out.println(">> MicroController Controller(micro-controller:POST)");
        System.out.println("RequestBody:" + microControllerPostParam);

        MicroControllerPostResponse microControllerPostResponse = microControllerService.addMicroControllerRelation(microControllerPostParam.getUserId(), microControllerPostParam.getMacAddress());

        System.out.println("<< MicroController Controller(micro-controller:POST)");
        return new ResponseEntity<>(microControllerPostResponse.getMicroController().getMacAddress(), microControllerPostResponse.getStatus());
    }
}
