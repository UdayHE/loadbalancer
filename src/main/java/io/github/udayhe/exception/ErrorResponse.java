package io.github.udayhe.exception;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;

@Getter
@Serdeable
public class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

}
