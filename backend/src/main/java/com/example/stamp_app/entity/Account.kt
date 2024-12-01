package com.example.stamp_app.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.util.*

@Entity
data class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    val uuid: @NotNull UUID? = null,

    @Column(unique = true, name = "mailAddress")
    val email: @NotNull @Pattern(regexp = "^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$") String? = null,

    @Column
    val password: @NotNull String? = null,

    @Column
    val name: String? = null,

    @Column
    val createdAt: LocalDateTime? = null,

    @Column
    val updatedAt: LocalDateTime? = null,

    @Column
    val deletedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "account")
    val microController: List<MicroController>? = null
){
}
