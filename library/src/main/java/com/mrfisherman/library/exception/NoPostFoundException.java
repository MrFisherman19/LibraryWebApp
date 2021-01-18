package com.mrfisherman.library.exception;

public class NoPostFoundException extends RuntimeException {

    public NoPostFoundException() { }

    public NoPostFoundException(String message) {
        super(message);
    }

    public NoPostFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
