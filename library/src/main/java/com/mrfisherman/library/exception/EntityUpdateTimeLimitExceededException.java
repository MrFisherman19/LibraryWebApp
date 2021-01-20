package com.mrfisherman.library.exception;

public class EntityUpdateTimeLimitExceededException extends RuntimeException {

    public EntityUpdateTimeLimitExceededException(String message) {
        super(message);
    }
}
