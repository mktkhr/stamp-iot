package com.example.stamp_app.repository;

import com.example.stamp_app.entity.Sdi12Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface Sdi12DataRepository extends JpaRepository<Sdi12Data, UUID> {
}
