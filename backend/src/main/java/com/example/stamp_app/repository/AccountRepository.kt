package com.example.stamp_app.repository

import com.example.stamp_app.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account?, UUID?> {

    fun findByEmail(email: String): Account?
    fun findByEmailAndDeletedAtIsNull(email: String): Account?
    fun findByUuid(uuid: UUID): Account?
    fun findById(id: Long): Account?

}
