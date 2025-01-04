package com.example.stamp_app.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

@Entity
data class Sensor (
    @Id
    val id: @Length(max = 3, min = 1) Long? = null,

    @Column
    val sensorName: @NotNull String? = null,

    @Column
    val vendorName: String? = null,

    @Column
    val identificationCode: String? = null,

    @OneToMany(mappedBy = "sensor")
    val sdi12Data: List<Sdi12Data>? = null
)
