package ru.anykeyers.configurationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Ошибка отсутствия автомойки
 */
public class ConfigurationNotFoundException extends ResponseStatusException {

    public ConfigurationNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Configuration with id " + id + " not found");
    }

}
