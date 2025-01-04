package com.example.stamp_app.controller.response

import com.example.stamp_app.entity.Account
import org.springframework.http.HttpStatus

data class AccountLoginResponse(val status: HttpStatus, @JvmField val account: Account)
