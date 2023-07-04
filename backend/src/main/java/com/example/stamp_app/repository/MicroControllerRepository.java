package com.example.stamp_app.repository;

import com.example.stamp_app.entity.MicroController;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MicroControllerRepository extends JpaRepository<MicroController, String> {
    MicroController findByMacAddress(String macAddress);

    MicroController findByUuid(String uuid);
}
