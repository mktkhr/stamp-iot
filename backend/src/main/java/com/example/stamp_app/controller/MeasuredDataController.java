package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MeasuredDataPostParam;
import com.example.stamp_app.controller.response.measuredDataGetResponse.MeasuredDataGetResponse;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.MeasuredDataService;
import com.example.stamp_app.session.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Tag(name = "MeasuredData", description = "測定結果関連API")
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
    @Operation(summary = "測定データ登録API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登録成功", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class))),
            @ApiResponse(responseCode = "400", description = "バリデーションエラー", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class))),
            @ApiResponse(responseCode = "401", description = "認証エラー", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class))),
            @ApiResponse(responseCode = "403", description = "無効なマイコン", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class)))
    })
    @PostMapping
    public ResponseEntity<HttpStatus> addMeasuredData(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "登録情報", content = {
                    @Content(schema = @Schema(implementation = MeasuredDataPostParam.class))
            })
            @RequestBody
            @Validated MeasuredDataPostParam measuredDataPostParam) {

        measuredDataService.addMeasuredData(measuredDataPostParam);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * アカウントIDとマイコンIDを指定して測定結果を取得するAPI
     *
     * @param microControllerUuid マイコンID
     * @return 測定結果
     */
    @Operation(summary = "測定データ取得API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "取得成功", content = @Content(schema = @Schema(implementation = MeasuredDataGetResponse.class))),
            @ApiResponse(responseCode = "400", description = "バリデーションエラー", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class))),
            @ApiResponse(responseCode = "403", description = "マイコン所有者不一致エラー", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class)))
    })
    @GetMapping
    public ResponseEntity<MeasuredDataGetResponse> getMeasuredData(
            @Parameter(required = true, description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
            @RequestParam
            @Pattern(regexp = "^([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})$") String microControllerUuid) {

        var userUuid = redisService.getUserUuidFromSessionUuid(requestedUser.getSessionUuid());

        var response = measuredDataService.getMeasuredData(userUuid, microControllerUuid);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
