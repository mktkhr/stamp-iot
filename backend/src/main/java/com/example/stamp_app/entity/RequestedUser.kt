package com.example.stamp_app.entity

import lombok.Data
import org.springframework.stereotype.Component

@Component // NOTE: Interceptorでsetするため，@Dataを使用する
data class RequestedUser (
    var sessionUuid: String? = null,
    var userUuid: String? = null
)
