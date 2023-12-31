package com.example.stamp_app.domain.exception;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class EMSResourceNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EMSResourceNotFoundException(String message) {
        super(message);
    }
}
