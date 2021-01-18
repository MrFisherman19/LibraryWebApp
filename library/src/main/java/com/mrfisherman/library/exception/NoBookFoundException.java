package com.mrfisherman.library.exception;

public class NoBookFoundException extends RuntimeException {

    public NoBookFoundException() { }

    public NoBookFoundException(String message) {
        super(message);
    }

    public NoBookFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
