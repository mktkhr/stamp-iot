package com.example.stamp_app.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.Comment
import java.time.LocalDateTime
import java.util.*

@Entity
data class MicroController(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column
	@Comment(value = "端末UUID")
	val uuid: @NotNull UUID? = null,

	@Column
	@Comment(value = "端末名")
	val name: String? = null,

	@Comment(value = "MACアドレス")
	val macAddress: @NotNull @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$") String? = null,

	@Column
	@ColumnDefault("60")
	@Comment(value = "測定間隔(分)")
	val interval: @Pattern(regexp = "^(60|30|20|15|10|5|1)$") String,

	@Column
	@Comment(value = "測定に使用するSDI-12アドレス(カンマ区切り)")
	val sdi12Address: @Pattern(regexp = "^((([0-9A-Za-z]{1},)+[0-9A-za-z]{1})|([0-9A-za-z]{1})?)$") String? = null,

	@Column
	val createdAt: LocalDateTime? = null,

	@Column
	val updatedAt: LocalDateTime? = null,

	@Column
	val deletedAt: LocalDateTime? = null,

	@OneToMany(mappedBy = "microController")
	val measuredDataMasters: List<MeasuredDataMaster>? = null,

	@ManyToOne
	val account: Account? = null
)
