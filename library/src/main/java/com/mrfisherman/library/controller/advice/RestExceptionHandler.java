package com.mrfisherman.library.controller.advice;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mrfisherman.library.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private final String VALIDATION_ERROR_MESSAGE = "Validation failed for given fields";

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleEntityNotFoundException(Exception e) {
        return new ApiErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return new ApiErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        var constraintViolations = e.getConstraintViolations().stream()
                .collect(Collectors.toMap(f -> String.valueOf(f.getPropertyPath()), ConstraintViolation::getMessage));
        e.getConstraintViolations().stream().map(ConstraintViolation::getInvalidValue).forEach(System.out::println);
        return new ApiValidationErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var constraintViolations = e.getBindingResult().getAllErrors().stream()
                .map(f -> (FieldError) f)
                .collect(Collectors.toMap(FieldError::getField, errorField ->
                        Optional.ofNullable(errorField.getDefaultMessage()).orElse("Validation failed for this field")));

        return new ApiValidationErrorResponse(HttpStatus.BAD_REQUEST, VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @ExceptionHandler(value = {InvalidTokenException.class, TokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleVerifyExceptions(InvalidTokenException e) {
        return new ApiErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
}
