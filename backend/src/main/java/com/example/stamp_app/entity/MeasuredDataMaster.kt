package com.example.stamp_app.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
data class MeasuredDataMaster(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val dayOfYear: String? = null,

    @Column
    val createdAt: LocalDateTime? = null,

    @Column
    val updatedAt: LocalDateTime? = null,

    @Column
    val deletedAt: LocalDateTime? = null,

    @ManyToOne
    val microController: MicroController? = null,

    @JsonManagedReference
    @OneToMany(mappedBy = "measuredDataMaster")
    val sdi12Data: List<Sdi12Data>? = null,

    @JsonManagedReference
    @OneToMany(mappedBy = "measuredDataMaster")
    val environmentalData: List<EnvironmentalData>? = null,

    @Column
    val voltage: String? = null,
)
