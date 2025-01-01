package com.example.stamp_app.entity

import org.springframework.stereotype.Component

@Component
data class RequestedUser(
	var sessionUuid: String? = null,
	var userUuid: String
)
