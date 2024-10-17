package ru.anykeyers.serviceofservices.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Исключение, что не существует услгуа
 */
public class ServiceNotFoundException extends ResponseStatusException {

    public ServiceNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Service not found with id: " + id);
    }

}
