package com.example.stamp_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sdi12Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

}
