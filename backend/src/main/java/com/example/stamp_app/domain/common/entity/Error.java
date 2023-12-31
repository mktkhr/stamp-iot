package com.example.stamp_app.domain.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {

    String code;
    String message;
}
