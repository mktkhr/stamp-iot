package com.example.stamp_app.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull

@Entity
data class Sdi12Data(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column
	val sdiAddress: @NotNull String,

	@Column(name = "volumetricWaterContent")
	val vwc: String? = null,

	@Column(name = "soilTemperature")
	val soilTemp: String? = null,

	@Column(name = "bulkRelativePermittivity")
	val brp: String? = null,

	@Column(name = "soilBulkElectricConductivity")
	val sbec: String? = null,

	@Column(name = "soilPoreWaterElectricConductivity")
	val spwec: String? = null,

	@Column(name = "gravitationalAccelerationXAxis")
	val gax: String? = null,

	@Column(name = "gravitationalAccelerationYAxis")
	val gay: String? = null,

	@Column(name = "gravitationalAccelerationZAxis")
	val gaz: String? = null,

	@JsonBackReference
	@ManyToOne
	val measuredDataMaster: MeasuredDataMaster? = null,

	@ManyToOne
	val sensor: Sensor? = null
)
