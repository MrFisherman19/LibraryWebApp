package com.mrfisherman.library.controller.advice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ApiValidationErrorResponse {

    private final int statusCode;
    private final String message;
    private final Map<String, String> errors = new HashMap<>();
    private final Instant timestamp = Instant.now();

    public ApiValidationErrorResponse(HttpStatus statusCode, String message, Map<String, String> errors) {
        this.statusCode = statusCode.value();
        this.message = message;
        errors.forEach(this.errors::put);
    }
}
