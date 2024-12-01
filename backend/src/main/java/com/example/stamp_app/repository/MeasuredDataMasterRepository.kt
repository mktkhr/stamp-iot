package com.example.stamp_app.repository

import com.example.stamp_app.entity.MeasuredDataMaster
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MeasuredDataMasterRepository : JpaRepository<MeasuredDataMaster?, UUID?> {

    override fun findAll(): List<MeasuredDataMaster>

}
