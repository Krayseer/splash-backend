package ru.anykeyers.orderservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Исключение, что не имеется свободных боксов
 */
public class BoxFreeNotFoundException extends ResponseStatusException {

    public BoxFreeNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Free box not found");
    }

}
