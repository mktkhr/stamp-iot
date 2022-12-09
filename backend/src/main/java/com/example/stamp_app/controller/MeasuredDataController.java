package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MeasureDataPostParam;
import com.example.stamp_app.service.MeasuredDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/ems/measured-data")
public class MeasuredDataController {

    @Autowired
    MeasuredDataService measuredDataService;

    /**
     * 測定データ登録API
     *
     * @param measureDataPostParam 測定データ
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<HttpStatus> addMeasuredData(@RequestBody MeasureDataPostParam measureDataPostParam) {
        System.out.println(">> Measured Data Controller(POST)");
        System.out.println("RequestBody:" + measureDataPostParam);

        var response = measuredDataService.addMeasuredData(measureDataPostParam);

        System.out.println("<< Measured Data Controller(POST)");
        return new ResponseEntity<>(response);
    }
}
