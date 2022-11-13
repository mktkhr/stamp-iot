package com.example.stamp_app.repository;

import com.example.stamp_app.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SensorRepository extends JpaRepository<Sensor, UUID> {
    Sensor findById(String sensorId);
}
