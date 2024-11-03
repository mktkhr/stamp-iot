package com.example.stamp_app.service;

import com.example.stamp_app.controller.param.MeasuredDataPostParam;
import com.example.stamp_app.controller.response.measuredDataGetResponse.EnvironmentalDataGetResponse;
import com.example.stamp_app.controller.response.measuredDataGetResponse.MeasuredDataGetResponse;
import com.example.stamp_app.controller.response.measuredDataGetResponse.Sdi12DataGetResponse;
import com.example.stamp_app.controller.response.measuredDataGetResponse.VoltageDataGetResponse;
import com.example.stamp_app.domain.exception.EMSDatabaseException;
import com.example.stamp_app.domain.exception.EMSResourceNotFoundException;
import com.example.stamp_app.entity.*;
import com.example.stamp_app.repository.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class MeasuredDataService {

    @Autowired
    MicroControllerRepository microControllerRepository;

    @Autowired
    MeasuredDataMasterRepository measuredDataMasterRepository;

    @Autowired
    Sdi12DataRepository sdi12DataRepository;

    @Autowired
    EnvironmentalDataRepository environmentalDataRepository;

    @Autowired
    SensorRepository sensorRepository;

    /**
     * 測定データ追加Service
     *
     * @param measuredDataPostParam 測定データ
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMeasuredData(@NotNull final MeasuredDataPostParam measuredDataPostParam) throws ResponseStatusException {

        // マイコンと所有者の一致確認
        log.info("測定データ送信元MACアドレス: " + measuredDataPostParam.getMacAddress());
        final var microController = microControllerRepository.findByMacAddress(measuredDataPostParam.getMacAddress());

        // DBに登録されていないマイコンの場合
        if (microController == null) {
            log.error("登録されていないマイコン");
            throw new EMSResourceNotFoundException();
        }

        final var accountId = microController.getAccount().getUuid().toString();
        log.info("測定データ登録対象のアカウントID: " + accountId);

        // 所有者UUIDがnullの場合401を返す
        if (accountId == null) {
            log.error("所有者の不一致");
            throw new EMSResourceNotFoundException(); // NOTE: 所有者の有無を秘匿するため，404で返す
        }

        // 測定時刻，DOYの算出
        final var measuredTime = LocalDateTime.now();
        final float doy = (float) LocalDate.now().getDayOfYear();
        final float hour = measuredTime.getHour();
        final float minutes = measuredTime.getMinute();
        final float seconds = measuredTime.getSecond();
        final float doyFloat = doy - 1 + (hour / 24) + (minutes / 1440) + (seconds / 1440 / 60);
        final String doyForData = String.valueOf(doyFloat);

        // パラメータの取り出し
        final var sdi12ParamList = measuredDataPostParam.getSdi12Param();
        final var environmentalDataList = measuredDataPostParam.getEnvironmentalDataParam();

        // 測定データマスターを作成
        final var measuredDataMaster = new MeasuredDataMaster(
                null,
                doyForData,
                measuredTime,
                measuredTime,
                null,
                microController,
                null,
                null,
                measuredDataPostParam.getVoltage()
        );

        // 測定データの保存
        measuredDataMasterRepository.save(measuredDataMaster);

        // SDI-12データの保存
        for (var sdi12Param : sdi12ParamList) {
            Sensor sensor = sensorRepository.findById(sdi12Param.getSensorId());
            final var sdi12Data = new Sdi12Data(
                    null,
                    sdi12Param.getSdiAddress(),
                    sdi12Param.getSdiAddress(),
                    sdi12Param.getSoilTemp(),
                    sdi12Param.getBrp(),
                    sdi12Param.getSbec(),
                    sdi12Param.getSpwec(),
                    sdi12Param.getGax(),
                    sdi12Param.getGay(),
                    sdi12Param.getGaz(),
                    measuredDataMaster,
                    sensor
            );
            sdi12DataRepository.save(sdi12Data);
        }

        // 環境データの保存
        for (var environmentalDataParam : environmentalDataList) {
            final var environmentalData = new EnvironmentalData(
                    null,
                    environmentalDataParam.getAirPress(),
                    environmentalDataParam.getTemp(),
                    environmentalDataParam.getHumi(),
                    environmentalDataParam.getCo2Concent(),
                    environmentalDataParam.getTvoc(),
                    environmentalDataParam.getAnalogValue(),
                    measuredDataMaster
            );
            environmentalDataRepository.save(environmentalData);
        }

    }

    /**
     * マイコンIDを指定して，対象の測定結果を取得
     *
     * @param userUuid            　ユーザーID
     * @param microControllerUuid マイコンUUID
     * @return 測定結果リスト
     */
    public MeasuredDataGetResponse getMeasuredData(@NotBlank final String userUuid, @NotBlank final String microControllerUuid) {

        final var microController = microControllerRepository.findByUuid(UUID.fromString(microControllerUuid));

        // マイコンが存在しない場合，400を返す
        if (microController == null) {
            throw new EMSResourceNotFoundException();
        }

        // マイコン保有者IDとパラメータ内のユーザーIDが異なる場合，403を返す
        if (!Objects.equals(microController.getAccount().getUuid(), UUID.fromString(userUuid))) {
            throw new EMSResourceNotFoundException();
        }

        // SDI-12の測定データ成形
        List<Sdi12DataGetResponse> sdi12DataGetResponseList = new ArrayList<>();

        final var microControllerId = microController.getId();

        // アカウントとマイコンIDに紐づくSDI-12アドレスのリストを取得する(マイコンに紐づくアカウントが変更された場合を考慮)
        final var sdi12AddressList = sdi12DataRepository.findSdiAddressGroupBySdiAddress(UUID.fromString(userUuid), microControllerId);

        // アドレスforeach
        sdi12AddressList.forEach((sdi12Address) -> {
            // アドレスを指定してデータを取得
            final var sdi12DataList = sdi12DataRepository.findBySdiAddress(sdi12Address);

            // マイコンIDとユーザーUUIDがリクエストと一致するデータを取得し，Sdi12DataAndDoyに変換
            final var convertedSdi12MeasuredDataList = sdi12DataList.stream().filter((measuredData) ->
                            Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getId(), microControllerId)
                                    && Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getAccount().getUuid().toString(), userUuid))
                    .map((data) -> Sdi12DataGetResponse.convertFromSdi12Data(data, data.getMeasuredDataMaster().getId(), data.getMeasuredDataMaster().getDayOfYear()))
                    .toList();

            final var sdi12DataGetResponse =  new Sdi12DataGetResponse(
                    sdi12Address,
                    convertedSdi12MeasuredDataList
            );

            sdi12DataGetResponseList.add(sdi12DataGetResponse);
        });

        // 環境データを全取得
        final var environmentalDataList = environmentalDataRepository.findAll();

        // アカウントIDとマイコンIDに紐づく環境データを取得
        final var convertedEnvironmentalMeasuredDataList = environmentalDataList.stream().filter((measuredData) ->
                        Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getId(), microControllerId)
                                && Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getAccount().getUuid().toString(), userUuid))
                .map((data) -> EnvironmentalDataGetResponse.convertFromEnvironmentalData(data, data.getMeasuredDataMaster().getId(), data.getMeasuredDataMaster().getDayOfYear()))
                .toList();

        // 電圧データ成形
        final var voltageDataList = measuredDataMasterRepository.findAll();

        final var convertedVoltageMeasuredDataList = voltageDataList.stream().filter((measuredData) ->
                        Objects.equals(measuredData.getMicroController().getId(), microControllerId)
                                && Objects.equals(measuredData.getMicroController().getAccount().getUuid().toString(), userUuid))
                .map(VoltageDataGetResponse::convertFromMeasuredDataMaster)
                .toList();

        return new MeasuredDataGetResponse(
                sdi12DataGetResponseList,
                convertedEnvironmentalMeasuredDataList,
                convertedVoltageMeasuredDataList
        );

    }

}
