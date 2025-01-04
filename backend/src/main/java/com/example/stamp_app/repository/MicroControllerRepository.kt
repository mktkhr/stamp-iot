package com.example.stamp_app.repository

import com.example.stamp_app.entity.MicroController
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MicroControllerRepository : JpaRepository<MicroController?, UUID?> {

    fun findByMacAddress(macAddress: String): MicroController?

    fun findByUuid(uuid: UUID): MicroController?

}
