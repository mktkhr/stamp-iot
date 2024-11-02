package com.example.stamp_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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

}
