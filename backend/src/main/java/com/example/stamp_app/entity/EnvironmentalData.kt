package com.example.stamp_app.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
data class EnvironmentalData (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "atmosphericPressure")
    val airPress: String? = null,

    @Column(name = "temperature")
    val temp: String? = null,

    @Column(name = "relativeHumidity")
    val humi: String? = null,

    @Column(name = "co2Concentration")
    val co2Concent: String? = null,

    @Column(name = "totalVolatileOrganicCompounds")
    val tvoc: String? = null,

    @Column(name = "analogValue")
    val analogValue: String? = null,

    @JsonBackReference
    @ManyToOne
    val measuredDataMaster: MeasuredDataMaster? = null
){}
