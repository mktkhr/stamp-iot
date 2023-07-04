package com.example.stamp_app.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class RequestedUser {
    private String sessionUuid;
    private String userUuid;
}
