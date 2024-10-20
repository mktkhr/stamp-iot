package com.example.stamp_app.entity;

import com.example.stamp_app.controller.param.EnvironmentalDataParam;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class EnvironmentalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atmosphericPressure")
    private String airPress;

    @Column(name = "temperature")
    private String temp;

    @Column(name = "relativeHumidity")
    private String humi;

    @Column(name = "co2Concentration")
    private String co2Concent;

    @Column(name = "totalVolatileOrganicCompounds")
    private String tvoc;

    @Column(name = "analogValue")
    private String analogValue;

    @JsonBackReference
    @ManyToOne
    private MeasuredDataMaster measuredDataMaster;


    /**
     * environmentalParamからEnvironmentalDataを作成する
     *
     * @param environmentalDataParam コントローラーが受け取ったパラメータ
     * @param measuredDataMaster 測定データマスター
     * @return environmentalData
     */
    public static EnvironmentalData createEnvironmentalData(EnvironmentalDataParam environmentalDataParam, MeasuredDataMaster measuredDataMaster){

        var environmentalData = new EnvironmentalData();
        environmentalData.setAirPress(environmentalDataParam.getAirPress());
        environmentalData.setTemp(environmentalDataParam.getTemp());
        environmentalData.setHumi(environmentalDataParam.getHumi());
        environmentalData.setCo2Concent(environmentalDataParam.getCo2Concent());
        environmentalData.setTvoc(environmentalDataParam.getTvoc());
        environmentalData.setAnalogValue(environmentalDataParam.getAnalogValue());
        environmentalData.setMeasuredDataMaster(measuredDataMaster);

        return environmentalData;

    }

    /**
     * EnvironmentalParamのリストからEnvironmentalDataのリストを作成する
     *
     * @param environmentalDataParamList EnvironmentalDataのリスト
     * @param measuredDataMaster 測定データマスター
     * @return environmentalDataList
     */
    public static List<EnvironmentalData> createEnvironmentalDataList(List<EnvironmentalDataParam> environmentalDataParamList, MeasuredDataMaster measuredDataMaster){

        var environmentalDataList = new ArrayList<EnvironmentalData>();

        for(var environmentalDataParam: environmentalDataParamList){
            var environmentalData = EnvironmentalData.createEnvironmentalData(environmentalDataParam, measuredDataMaster);
            environmentalDataList.add(environmentalData);
        }

        return environmentalDataList;

    }
}
