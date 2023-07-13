package com.example.stamp_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
public class Sensor {

    @Id
    @Length(max = 3, min = 1)
    private Long id;

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
