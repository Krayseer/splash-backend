package ru.anykeyers.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Исключение об уже существующем пользователе
 */
public class UserAlreadyExistsException extends ResponseStatusException {

    public UserAlreadyExistsException(String username) {
        super(HttpStatus.CONFLICT, "User already exists: " + username);
    }

}
