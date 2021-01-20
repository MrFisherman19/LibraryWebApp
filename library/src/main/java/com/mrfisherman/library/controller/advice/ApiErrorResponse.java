package com.mrfisherman.library.controller.advice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.Instant;

@Getter
@Setter
public class ApiErrorResponse {

    private final int statusCode;
    private final String message;
    private final Instant timestamp = Instant.now();

    public ApiErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

}
