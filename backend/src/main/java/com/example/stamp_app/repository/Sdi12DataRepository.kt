package com.example.stamp_app.repository

import com.example.stamp_app.entity.Sdi12Data
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface Sdi12DataRepository : JpaRepository<Sdi12Data?, UUID?> {

    @Query(value = "SELECT sdi_address " +
            "FROM sdi12data " +
            "LEFT JOIN measured_data_master " +
            "ON sdi12data.measured_data_master_id = measured_data_master.id " +
            "LEFT JOIN micro_controller " +
            "ON measured_data_master.micro_controller_id = micro_controller.id " +
            "LEFT JOIN account " +
            "ON micro_controller.account_id = account.id " +
            "WHERE account.uuid = :#{#userUuid} " +
            "AND micro_controller.id = :#{#microControllerId} " +
            "GROUP BY sdi12data.sdi_address", nativeQuery = true)
    fun findSdiAddressGroupBySdiAddress(@Param("userUuid") userUuid: UUID?, @Param("microControllerId") microControllerId: Long?): List<String?>?

    fun findBySdiAddress(sdiAddress: String?): List<Sdi12Data?>?

}
