package com.example.stamp_app.repository

import com.example.stamp_app.entity.Sensor
import org.springframework.data.jpa.repository.JpaRepository

interface SensorRepository : JpaRepository<Sensor?, String?> {

    fun findById(sensorId: Long): Sensor?

}
