package com.example.stamp_app.entity;

import com.example.stamp_app.controller.param.Sdi12Param;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Sdi12Data {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NotNull
    @Column
    private String sdiAddress;

    @Column(name = "volumetricWaterContent")
    private String vwc;

    @Column(name = "soilTemperature")
    private String soilTemp;

    @Column(name = "bulkRelativePermittivity")
    private String brp;

    @Column(name = "soilBulkElectricConductivity")
    private String sbec;

    @Column(name = "soilPoreWaterElectricConductivity")
    private String spwec;

    @Column(name = "gravitationalAccelerationXAxis")
    private String gax;

    @Column(name = "gravitationalAccelerationYAxis")
    private String gay;

    @Column(name = "gravitationalAccelerationZAxis")
    private String gaz;

    @JsonBackReference
    @ManyToOne
    private MeasuredDataMaster measuredDataMaster;

    @ManyToOne
    private Sensor sensor;

    /**
     * Sdi12ParamからSdi12Dataを作成する
     *
     * @param sdi12Param コントローラーが受け取ったSDI-12パラメータ
     * @param measuredDataMaster 測定データマスター
     * @return Sdi12Data
     */
    public static Sdi12Data createSdi12Data(Sdi12Param sdi12Param, MeasuredDataMaster measuredDataMaster, Sensor sensor){
        var sdi12Data = new Sdi12Data();
        sdi12Data.setSdiAddress(sdi12Param.getSdiAddress());
        sdi12Data.setVwc(sdi12Param.getVwc());
        sdi12Data.setSoilTemp(sdi12Param.getSoilTemp());
        sdi12Data.setBrp(sdi12Param.getBrp());
        sdi12Data.setSbec(sdi12Param.getSbec());
        sdi12Data.setSpwec(sdi12Param.getSpwec());
        sdi12Data.setGax(sdi12Param.getGax());
        sdi12Data.setGay(sdi12Param.getGay());
        sdi12Data.setGaz(sdi12Param.getGaz());
        sdi12Data.setMeasuredDataMaster(measuredDataMaster);
        sdi12Data.setSensor(sensor);
        return sdi12Data;
    }


    /**
     * Sdi12ParamのリストからSdi12Dataのリストを作成する
     *
     * @param sdi12ParamList SDI-12パラメータのリスト
     * @param measuredDataMaster 測定データマスター
     * @return List<Sdi12Data>
     */
    public static List<Sdi12Data> createSdi12DataList(List<Sdi12Param> sdi12ParamList, MeasuredDataMaster measuredDataMaster, Sensor sensor){

        var sdi12DataList = new ArrayList<Sdi12Data>();

        for(var sdi12Param: sdi12ParamList){
            var sdi12Data = Sdi12Data.createSdi12Data(sdi12Param, measuredDataMaster, sensor);
            sdi12DataList.add(sdi12Data);
        }

        return sdi12DataList;

    }
}
