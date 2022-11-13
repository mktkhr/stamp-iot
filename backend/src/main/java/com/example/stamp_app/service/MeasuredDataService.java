package com.example.stamp_app.service;

import com.example.stamp_app.controller.Param.MeasureDataPostParam;
import com.example.stamp_app.entity.*;
import com.example.stamp_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
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
     * @param measureDataPostParam 測定データ
     * @return HttpStatus
     */
    public HttpStatus addMeasuredData(MeasureDataPostParam measureDataPostParam){

        MicroController microController;

        // 必要なパラメータが不足していた場合
        if(measureDataPostParam.getOwnerUuid() == null || measureDataPostParam.getMacAddress() == null){
            return HttpStatus.BAD_REQUEST;
        }

        // マイコンと所有者の一致確認
        try{
            // microControllerをprintしてはいけない理由(https://blogenist.jp/2020/12/17/11185/#i)
            microController = microControllerRepository.findByMacAddress(measureDataPostParam.getMacAddress());

            // DBに登録されていないマイコンの場合
            if(microController == null){
                return HttpStatus.FORBIDDEN;
            }

            var accountId = microController.getAccount().getUuid().toString();
            System.out.println("登録対象のアカウントID: " + accountId);

            // 所有者が一致しない場合401を返す
            if(!measureDataPostParam.getOwnerUuid().equals(accountId)){
                return HttpStatus.UNAUTHORIZED;
            }

        } catch(Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        var dataMasterId = UUID.randomUUID();

        // 測定時刻，DOYの算出
        var measuredTime = LocalDateTime.now();
        float doy = (float) LocalDate.now().getDayOfYear();
        float hour = measuredTime.getHour();
        float minutes = measuredTime.getMinute();
        float seconds = measuredTime.getSecond();
        float doyFloat = doy - 1 + (hour / 24) + (minutes / 1440) + (seconds / 1440 / 60);
        String doyForData = String.valueOf(doyFloat);

        // パラメータの取り出し
        var sdi12ParamList = measureDataPostParam.getSdi12Param();
        var environmentalDataList = measureDataPostParam.getEnvironmentalDataParam();

        // 測定データマスターを作成
        var measuredDataMaster = new MeasuredDataMaster();
        measuredDataMaster.setUuid(dataMasterId);
        measuredDataMaster.setDayOfYear(doyForData);
        measuredDataMaster.setCreatedAt(measuredTime);
        measuredDataMaster.setVoltage(measureDataPostParam.getVoltage());
        measuredDataMaster.setMicroController(microController);

        // 測定データの保存
        try{
            measuredDataMasterRepository.save(measuredDataMaster);

            for(var sdi12Param: sdi12ParamList){
                Sensor sensor = sensorRepository.findById(sdi12Param.getSensorId());
                sdi12DataRepository.save(Sdi12Data.createSdi12Data(sdi12Param, measuredDataMaster, sensor));
            }

            for(var environmentalDataParam: environmentalDataList){
                environmentalDataRepository.save(EnvironmentalData.createEnvironmentalData(environmentalDataParam, measuredDataMaster));
            }

        }catch(Exception e){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.OK;
    }
}
