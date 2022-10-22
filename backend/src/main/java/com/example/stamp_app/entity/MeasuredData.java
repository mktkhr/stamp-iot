package com.example.stamp_app.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeasuredData {

    @Id
    @NotNull
    private UUID ulid;

    @ManyToOne
    private MeasuredTime measuredTime;

    @ManyToOne
    private Sensor sensor;

    @OneToMany(mappedBy = "measuredData")
    private List<MeasuredData> measuredData;

    @Column
    private String sdiAddress;

    @Column
    private Float volumetricWaterContent;

    @Column
    private Float soilTemperature;

    @Column
    private Float bulkRelativePermittivity;

    @Column
    private Float soilBulkElectricConductivity;

    @Column
    private Float soilPoreWaterElectricConductivity;

    @Column
    private Float gravitationalAccelerationXAxis;

    @Column
    private Float gravitationalAccelerationYAxis;

    @Column
    private Float gravitationalAccelerationZAxis;

    @Column
    private Float atmosphericPressure;

    @Column
    private Float temperature;

    @Column
    private Float relativeHumidity;

    @Column
    private Float co2Concentration;

    @Column
    private Float totalVolatileOrganicCompounds;

    @Column
    private Float analogValue;

    @Column
    private Float voltage;
}
