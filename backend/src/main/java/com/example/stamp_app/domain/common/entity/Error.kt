package com.example.stamp_app.domain.common.entity

import lombok.Getter

@Getter
data class Error(
	val code: String? = null,
	val message: String? = null
)
