package com.example.stamp_app.repository;

import com.example.stamp_app.entity.MicroController;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MicroControllerRepository extends JpaRepository<MicroController, UUID> {
    MicroController findByMacAddress(String macAddress);

    MicroController findByUuid(UUID uuid);
}
