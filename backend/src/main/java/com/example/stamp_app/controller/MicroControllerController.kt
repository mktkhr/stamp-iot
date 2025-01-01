package com.example.stamp_app.controller

import com.example.stamp_app.controller.param.MicroControllerPostParam
import com.example.stamp_app.controller.param.microController.MicroControllerPatchParam
import com.example.stamp_app.controller.response.MicroControllerGetResponse
import com.example.stamp_app.controller.response.MicroControllerPostResponse
import com.example.stamp_app.controller.response.microController.MicroControllerGetDetailNoSessionResponse
import com.example.stamp_app.entity.RequestedUser
import com.example.stamp_app.service.MicroControllerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import org.apache.commons.lang3.ObjectUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@Tag(name = "MicroController", description = "マイコン関連API")
@RequestMapping(value = ["/ems/micro-controller"])
class MicroControllerController(
	private val microControllerService: MicroControllerService,
	private val requestedUser: RequestedUser
) {

	/**
	 * マイコン登録API
	 */
	@Operation(summary = "マイコン登録API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "登録成功",
			content = [Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = Schema(implementation = MicroControllerPostResponse::class)
			)]
		), ApiResponse(
			responseCode = "400",
			description = "バリデーションエラー/無効なマイコン",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		), ApiResponse(
			responseCode = "401",
			description = "認証エラー",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@PostMapping
	fun addMicroControllerRelation(
		@RequestBody(
			description = "マイコン登録パラメータ",
			content = [Content(schema = Schema(implementation = MicroControllerPostParam::class))]
		) @org.springframework.web.bind.annotation.RequestBody @Validated microControllerPostParam: MicroControllerPostParam
	): ResponseEntity<MicroControllerPostResponse> {
		val microControllerPostResponse = microControllerService
			.addMicroControllerRelation(
				microControllerPostParam.userId,
				microControllerPostParam.macAddress
			)
		return ResponseEntity(microControllerPostResponse, HttpStatus.OK)
	}

	@get:GetMapping(value = ["/info"])
	@get:ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "取得成功",
			content = [Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				array = ArraySchema(
					schema = Schema(
						implementation = MicroControllerGetResponse::class
					)
				)
			)]
		), ApiResponse(
			responseCode = "400",
			description = "無効なアカウント",
			content = [Content(
				schema = Schema(implementation = ObjectUtils.Null::class)
			)]
		)]
	)
	@get:Operation(summary = "マイコン一覧取得API")
	val microControllerInfo: ResponseEntity<List<MicroControllerGetResponse>>
		/**
		 * アカウントに紐づくマイコン一覧取得API
		 */
		get() {
			val microControllerList = microControllerService.getMicroControllerList(
				requestedUser.userUuid!!
			)
			return ResponseEntity(microControllerList, HttpStatus.OK)
		}

	/**
	 * マイコン詳細情報取得
	 *
	 * @param microControllerUuid マイコンUUID
	 */
	@Operation(summary = "マイコン詳細取得API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "取得成功",
			content = [Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = Schema(implementation = MicroControllerGetResponse::class)
			)]
		), ApiResponse(
			responseCode = "400",
			description = "不正なリクエスト内容　",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		), ApiResponse(
			responseCode = "403",
			description = "権限のない操作",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@GetMapping(value = ["/detail"])
	fun getMicroControllerDetail(
		@Parameter(
			required = true,
			description = "マイコンUUID",
			example = "61d5f7a7-7629-496e-be68-cfe022264578"
		) @RequestParam microControllerUuid: @Pattern(regexp = "^([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{12})$") String?
	): ResponseEntity<MicroControllerGetResponse> {
		val microController = microControllerService.getMicroControllerDetail(microControllerUuid!!)
		val response = MicroControllerGetResponse.convertMicroControllerToDetailResponse(microController)
		return ResponseEntity(response, HttpStatus.OK)
	}

	/**
	 * マイコン詳細情報取得(セッション無し)
	 *
	 * @param macAddress マイコンMACアドレス
	 */
	@Operation(summary = "マイコン詳細取得API(セッション無し)")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "取得成功",
			content = [Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				schema = Schema(implementation = MicroControllerGetDetailNoSessionResponse::class)
			)]
		), ApiResponse(
			responseCode = "400",
			description = "無効なリクエスト",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@GetMapping(value = ["/detail/no-session"])
	fun getMicroControllerDetailWithoutSession(
		@Parameter(
			required = true,
			description = "マイコンUUID",
			example = "AA:AA:AA:AA:AA:AA"
		) @RequestParam macAddress: @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$") String?
	): ResponseEntity<MicroControllerGetDetailNoSessionResponse> {
		val microController = microControllerService.getMicroControllerDetailWithMacAddress(macAddress!!)
		val response = MicroControllerGetDetailNoSessionResponse
			.convertMicroControllerToDetailResponse(microController)
		return ResponseEntity(response, HttpStatus.OK)
	}

	/**
	 * マイコン詳細更新API
	 *
	 * @param param 更新パラメータ
	 */
	@Operation(summary = "マイコン詳細更新API")
	@ApiResponses(
		value = [ApiResponse(
			responseCode = "200",
			description = "取得成功",
			content = [Content(
				mediaType = MediaType.APPLICATION_JSON_VALUE,
				array = ArraySchema(schema = Schema(implementation = MicroControllerGetResponse::class))
			)]
		), ApiResponse(
			responseCode = "400",
			description = "無効なアカウント",
			content = [Content(schema = Schema(implementation = ObjectUtils.Null::class))]
		)]
	)
	@PatchMapping(value = ["/detail"])
	fun updateMicroControllerDetail(
		@RequestBody(
			description = "マイコン詳細更新パラメータ",
			content = [Content(schema = Schema(implementation = MicroControllerPatchParam::class))]
		)
		@Valid
		@org.springframework.web.bind.annotation.RequestBody
		param: MicroControllerPatchParam
	): ResponseEntity<MicroControllerGetResponse> {
		val microController = microControllerService.updateMicroControllerDetail(
			requestedUser.userUuid!!,
			param!!
		)
		val response = MicroControllerGetResponse.convertMicroControllerToDetailResponse(microController!!)
		return ResponseEntity(response, HttpStatus.OK)
	}
}
