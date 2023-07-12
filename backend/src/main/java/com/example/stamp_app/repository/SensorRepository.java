package com.example.stamp_app.repository;

import com.example.stamp_app.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    Sensor findById(Long sensorId);
}
