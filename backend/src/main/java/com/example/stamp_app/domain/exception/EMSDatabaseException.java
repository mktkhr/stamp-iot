package com.example.stamp_app.domain.exception;

import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * データベースとの接続関連例外
 */
@NoArgsConstructor
public class EMSDatabaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public EMSDatabaseException(String message) {
        super(message);
    }

}
