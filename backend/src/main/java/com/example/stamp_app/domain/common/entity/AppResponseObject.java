package com.example.stamp_app.domain.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AppResponseObject {

    Object response;

    List<Error> errors = new ArrayList<>();

    public static AppResponseObject createErrorResponse(String code, String message) {
        var response = new AppResponseObject();
        var error = new Error(code, message);
        response.addError(error);
        return response;
    }

    public void addError(Error error) {

        this.errors.add(error);

    }

}
