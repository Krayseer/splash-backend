package ru.anykeyers.configurationservice.exception;

import java.util.UUID;

/**
 * У пользователя не имеется автомоек
 */
public class UserNotFoundConfigurationException extends RuntimeException {

    public UserNotFoundConfigurationException(UUID userId) {
        super(String.format("Configuration for user %s not found", userId));
    }

}
