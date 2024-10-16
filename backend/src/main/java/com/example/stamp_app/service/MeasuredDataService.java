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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Transactional(rollbackOn = Exception.class)
    public void addMeasuredData(MeasuredDataPostParam measuredDataPostParam) throws ResponseStatusException {

        // マイコンと所有者の一致確認
        // microControllerをprintしてはいけない理由(https://blogenist.jp/2020/12/17/11185/#i)
        log.info("測定データ送信元MACアドレス: " + measuredDataPostParam.getMacAddress());
        var microController = microControllerRepository.findByMacAddress(measuredDataPostParam.getMacAddress());

        // DBに登録されていないマイコンの場合
        if (microController == null) {
            log.error("登録されていないマイコン");
            throw new EMSResourceNotFoundException();
        }

        var accountId = microController.getAccount().getUuid().toString();
        log.info("測定データ登録対象のアカウントID: " + accountId);

        // 所有者UUIDがnullの場合401を返す
        if (accountId == null) {
            log.error("所有者の不一致");
            throw new EMSResourceNotFoundException(); // NOTE: 所有者の有無を秘匿するため，404で返す
        }

        // 測定時刻，DOYの算出
        var measuredTime = LocalDateTime.now();
        float doy = (float) LocalDate.now().getDayOfYear();
        float hour = measuredTime.getHour();
        float minutes = measuredTime.getMinute();
        float seconds = measuredTime.getSecond();
        float doyFloat = doy - 1 + (hour / 24) + (minutes / 1440) + (seconds / 1440 / 60);
        String doyForData = String.valueOf(doyFloat);

        // パラメータの取り出し
        var sdi12ParamList = measuredDataPostParam.getSdi12Param();
        var environmentalDataList = measuredDataPostParam.getEnvironmentalDataParam();

        // 測定データマスターを作成
        var measuredDataMaster = new MeasuredDataMaster();
        measuredDataMaster.setDayOfYear(doyForData);
        measuredDataMaster.setVoltage(measuredDataPostParam.getVoltage());
        measuredDataMaster.setMicroController(microController);

        // 測定データの保存
        measuredDataMasterRepository.save(measuredDataMaster);

        for (var sdi12Param : sdi12ParamList) {
            Sensor sensor = sensorRepository.findById(sdi12Param.getSensorId());
            sdi12DataRepository.save(Sdi12Data.createSdi12Data(sdi12Param, measuredDataMaster, sensor));
        }

        for (var environmentalDataParam : environmentalDataList) {
            environmentalDataRepository.save(EnvironmentalData.createEnvironmentalData(environmentalDataParam, measuredDataMaster));
        }

    }

    /**
     * マイコンIDを指定して，対象の測定結果を取得
     *
     * @param userUuid            　ユーザーID
     * @param microControllerUuid マイコンUUID
     * @return 測定結果リスト
     */
    public MeasuredDataGetResponse getMeasuredData(String userUuid, String microControllerUuid) {

        var microController = microControllerRepository.findByUuid(UUID.fromString(microControllerUuid));

        // マイコンが存在しない場合，400を返す
        if (microController == null) {
            throw new EMSResourceNotFoundException();
        }

        // マイコン保有者IDとパラメータ内のユーザーIDが異なる場合，403を返す
        if (!Objects.equals(microController.getAccount().getUuid(), UUID.fromString(userUuid))) {
            throw new EMSResourceNotFoundException();
        }

        // 大枠クラスの定義
        MeasuredDataGetResponse measuredDataGetResponse = new MeasuredDataGetResponse();

        // SDI-12の測定データ成形
        List<Sdi12DataGetResponse> sdi12DataGetResponseList = new ArrayList<>();

        var microControllerId = microController.getId();

        // アカウントとマイコンIDに紐づくSDI-12アドレスのリストを取得する(マイコンに紐づくアカウントが変更された場合を考慮)
        var sdi12AddressList = sdi12DataRepository.findSdiAddressGroupBySdiAddress(UUID.fromString(userUuid), microControllerId);

        // アドレスforeach
        sdi12AddressList.forEach((sdi12Address) -> {
            Sdi12DataGetResponse sdi12DataGetResponse = new Sdi12DataGetResponse();
            // アドレスを指定してデータを取得
            var sdi12DataList = sdi12DataRepository.findBySdiAddress(sdi12Address);

            // マイコンIDとユーザーUUIDがリクエストと一致するデータを取得し，Sdi12DataAndDoyに変換
            var convertedSdi12MeasuredDataList = sdi12DataList.stream().filter((measuredData) ->
                            Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getId(), microControllerId)
                                    && Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getAccount().getUuid().toString(), userUuid))
                    .map((data) -> Sdi12DataGetResponse.convertFromSdi12Data(data, data.getMeasuredDataMaster().getId(), data.getMeasuredDataMaster().getDayOfYear()))
                    .toList();

            sdi12DataGetResponse.setSdiAddress(sdi12Address);
            sdi12DataGetResponse.setDataList(convertedSdi12MeasuredDataList);

            sdi12DataGetResponseList.add(sdi12DataGetResponse);
        });

        measuredDataGetResponse.setSdi12Data(sdi12DataGetResponseList);

        // 環境データを全取得
        var environmentalDataList = environmentalDataRepository.findAll();

        // アカウントIDとマイコンIDに紐づく環境データを取得
        var convertedEnvironmentalMeasuredDataList = environmentalDataList.stream().filter((measuredData) ->
                        Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getId(), microControllerId)
                                && Objects.equals(measuredData.getMeasuredDataMaster().getMicroController().getAccount().getUuid().toString(), userUuid))
                .map((data) -> EnvironmentalDataGetResponse.convertFromEnvironmentalData(data, data.getMeasuredDataMaster().getId(), data.getMeasuredDataMaster().getDayOfYear()))
                .toList();

        measuredDataGetResponse.setEnvironmentalData(convertedEnvironmentalMeasuredDataList);

        // 電圧データ成形
        var voltageDataList = measuredDataMasterRepository.findAll();

        var convertedVoltageMeasuredDataList = voltageDataList.stream().filter((measuredData) ->
                        Objects.equals(measuredData.getMicroController().getId(), microControllerId)
                                && Objects.equals(measuredData.getMicroController().getAccount().getUuid().toString(), userUuid))
                .map(VoltageDataGetResponse::convertFromMeasuredDataMaster)
                .toList();

        measuredDataGetResponse.setVoltageData(convertedVoltageMeasuredDataList);

        return measuredDataGetResponse;

    }

}
