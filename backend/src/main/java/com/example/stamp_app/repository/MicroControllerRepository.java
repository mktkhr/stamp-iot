package com.example.stamp_app.repository;

import com.example.stamp_app.entity.MicroController;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.UUID;

public interface MicroControllerRepository extends JpaRepository<MicroController, UUID> {
    MicroController findByMacAddress(String macAddress);

    MicroController findById(BigInteger id);
}
