package ru.anykeyers.configurationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Ошибка отсутствия бокса
 */
public class BoxNotFoundException extends ResponseStatusException {

    public BoxNotFoundException(Long boxId) {
        super(HttpStatus.NOT_FOUND, "Box not found: " + boxId);
    }

}
