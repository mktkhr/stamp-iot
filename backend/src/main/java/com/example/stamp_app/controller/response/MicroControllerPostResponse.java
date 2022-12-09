package com.example.stamp_app.controller.response;

import com.example.stamp_app.entity.MicroController;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class MicroControllerPostResponse {

    HttpStatus status;

    MicroController microController;
}
