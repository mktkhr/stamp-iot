package com.example.stamp_app.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Sensor {

    @Id
    @NotNull
    @Length(max = 3, min = 1)
    private String id;

    @Column
    @NotNull
    private String sensorName;

    @Column
    private String vendorName;

    @Column
    private String identificationCode;

    @OneToMany(mappedBy = "sensor")
    private List<Sdi12Data> sdi12Data;

}
