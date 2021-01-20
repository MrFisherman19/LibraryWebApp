package com.mrfisherman.library.service;

import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.function.Supplier;
import static java.lang.String.format;

@Component
public class ExceptionHelper<T> {

    Supplier<EntityNotFoundException> getEntityNotFoundException(Long id, Class<T> entityType) {
        return () -> new EntityNotFoundException(format("Cannot find %s with given id: %d", entityType.getSimpleName(), id));
    }

}
