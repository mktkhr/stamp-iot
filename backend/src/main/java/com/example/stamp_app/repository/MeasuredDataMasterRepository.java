package com.example.stamp_app.repository;

import com.example.stamp_app.dummyData.MeasuredDataMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeasuredDataMasterRepository extends JpaRepository<MeasuredDataMaster, UUID> {
}
