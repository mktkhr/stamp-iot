package com.example.stamp_app.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
// NOTE: Interceptorでsetするため，@Dataを使用する
@Data
public class RequestedUser {
    private String sessionUuid;
    private String userUuid;
}
