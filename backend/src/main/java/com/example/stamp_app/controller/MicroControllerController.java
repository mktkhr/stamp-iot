package com.example.stamp_app.controller;

import com.example.stamp_app.controller.param.MicroControllerPostParam;
import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam;
import com.example.stamp_app.controller.response.MicroControllerGetResponse;
import com.example.stamp_app.controller.response.MicroControllerPostResponse;
import com.example.stamp_app.entity.RequestedUser;
import com.example.stamp_app.service.MicroControllerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@Tag(name = "MicroController", description = "マイコン関連API")
@RequestMapping(value = "/ems/micro-controller")
public class MicroControllerController {
    @Autowired
    MicroControllerService microControllerService;
    @Autowired
    RequestedUser requestedUser;

    /**
     * マイコン登録API
     */
    @Operation(summary = "マイコン登録API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登録成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MicroControllerPostResponse.class))),
            @ApiResponse(responseCode = "400", description = "バリデーションエラー/無効なマイコン", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class))),
            @ApiResponse(responseCode = "401", description = "認証エラー", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class)))
    })
    @PostMapping
    public ResponseEntity<MicroControllerPostResponse> addMicroControllerRelation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "マイコン登録パラメータ", content = {
                    @Content(schema = @Schema(implementation = MicroControllerPostParam.class))
            })
            @RequestBody
            @Validated MicroControllerPostParam microControllerPostParam) {

        MicroControllerPostResponse microControllerPostResponse = microControllerService.addMicroControllerRelation(microControllerPostParam.getUserId(), microControllerPostParam.getMacAddress());

        return new ResponseEntity<>(microControllerPostResponse, HttpStatus.OK);
    }

    /**
     * アカウントに紐づくマイコン一覧取得API
     */
    @Operation(summary = "マイコン一覧取得API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "取得成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MicroControllerGetResponse.class)))),
            @ApiResponse(responseCode = "400", description = "無効なアカウント", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class)))
    })
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
    @Operation(summary = "マイコン詳細取得API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "取得成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MicroControllerGetResponse.class)))),
            @ApiResponse(responseCode = "400", description = "無効なアカウント", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class)))
    })
    @GetMapping(value = "/detail")
    public ResponseEntity<MicroControllerGetResponse> getMicroControllerDetail(
            @Parameter(required = true, description = "マイコンUUID", example = "61d5f7a7-7629-496e-be68-cfe022264578")
            @RequestParam
            @Pattern(regexp = "^([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})$") String microControllerUuid) {
        var microController = microControllerService.getMicroControllerDetail(microControllerUuid);
        var response = MicroControllerGetResponse.convertMicroControllerToDetailResponse(microController);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * マイコン詳細更新API
     *
     * @param param 更新パラメータ
     */
    @Operation(summary = "マイコン詳細更新API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "取得成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = MicroControllerGetResponse.class)))),
            @ApiResponse(responseCode = "400", description = "無効なアカウント", content = @Content(schema = @Schema(implementation = ObjectUtils.Null.class)))
    })
    @PatchMapping(value = "/detail")
    public ResponseEntity<MicroControllerGetResponse> updateMicroControllerDetail(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "マイコン詳細更新パラメータ", content = {
                    @Content(schema = @Schema(implementation = MicroControllerPatchParam.class))
            })
            @Valid @Validated
            @RequestBody MicroControllerPatchParam param) {

        var microController = microControllerService.updateMicroControllerDetail(requestedUser.getUserUuid(), param);
        var response = MicroControllerGetResponse.convertMicroControllerToDetailResponse(microController);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
