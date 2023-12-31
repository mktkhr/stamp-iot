package com.example.stamp_app.domain.exception;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class EMSResourceDuplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EMSResourceDuplicationException(String message) {
        super(message);
    }
}
