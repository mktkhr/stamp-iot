package com.example.stamp_app.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sensor {

    @Id
    @NotNull
    private Integer id;

    @Column
    @NotNull
    private String sensorName;

    @Column
    private String vendorName;

    @Column
    private String identificationCode;

    @OneToMany(mappedBy = "sensor")
    private List<MeasuredData> measuredData;
}
