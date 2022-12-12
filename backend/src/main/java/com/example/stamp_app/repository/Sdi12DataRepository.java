package com.example.stamp_app.repository;

import com.example.stamp_app.entity.Sdi12Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface Sdi12DataRepository extends JpaRepository<Sdi12Data, UUID> {

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
            "GROUP BY sdi12data.sdi_address"
            , nativeQuery = true
    )
    List<String> findSdiAddressGroupBySdiAddress(@Param("userUuid") UUID userUuid, @Param("microControllerId") BigInteger microControllerId);

    // TODO: クラスに直接マッピングできるようにする
//    @Query(value = "SELECT measured_data_master.id, measured_data_master.day_of_year, sdi12data.volumetric_water_content, sdi12data.soil_temperature, sdi12data.bulk_relative_permittivity, sdi12data.soil_bulk_electric_conductivity, sdi12data.soil_pore_water_electric_conductivity, sdi12data.gravitational_accelerationxaxis, sdi12data.gravitational_accelerationyaxis, sdi12data.gravitational_accelerationzaxis, measured_data_master.created_at, measured_data_master.updated_at, measured_data_master.deleted_at " +
//            "FROM sdi12data " +
//            "LEFT JOIN measured_data_master " +
//            "ON sdi12data.measured_data_master_id = measured_data_master.id " +
//            "LEFT JOIN micro_controller " +
//            "ON measured_data_master.micro_controller_id = micro_controller.id " +
//            "LEFT JOIN account " +
//            "ON micro_controller.account_id = account.id " +
//            "WHERE account.uuid = :#{#userUuid} " +
//            "AND sdi12data.sdi_address = :#{#sdi12Address} " +
//            "AND micro_controller.id = :#{#microControllerId}"
//            , nativeQuery = true
//    )
//    @SqlResultSetMapping(name="sdi12Mapping",entities = @EntityResult(entityClass = Sdi12DataGetResponse.class, fields = {
//            @FieldResult(name="id",column = "measuredDataMasterId"),
//            @FieldResult(name="day_of_year",column = "dayOfYear"),
//            @FieldResult(name="volumetric_water_content",column = "volumetricWaterContent"),
//            @FieldResult(name="soil_temperature",column = "soilTemperature"),
//            @FieldResult(name="bulk_relative_permittivity",column = "bulkRelativePermittivity"),
//            @FieldResult(name="soil_bulk_electric_conductivity",column = "soilBulkElectricConductivity"),
//            @FieldResult(name="soil_pore_water_electric_conductivity",column = "soilPoreWaterElectricConductivity"),
//            @FieldResult(name="gravitational_accelerationxaxis",column = "gravitationalAccelerationXAxis"),
//            @FieldResult(name="gravitational_accelerationyaxis",column = "gravitationalAccelerationYAxis"),
//            @FieldResult(name="gravitational_accelerationzaxis",column = "gravitationalAccelerationZAxis"),
//            @FieldResult(name="created_at",column = "createdAt"),
//            @FieldResult(name="updated_at",column = "updatedAt"),
//            @FieldResult(name="deleted_at",column = "deletedAt"),
//    }))
//    List<Sdi12DataGetResponse.Sdi12DataAndDoy> findMeasuredDataBySdiAddress(@Param("userUuid") UUID userUuid, @Param("sdi12Address") String sdi12Address, @Param("microControllerId") BigInteger microControllerId);

    List<Sdi12Data> findBySdiAddress(String sdiAddress);
}
