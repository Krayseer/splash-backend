package ru.anykeyers.orderservice.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Ошибка получения заказа
 */
public class OrderNotFoundException extends ResponseStatusException {

    public OrderNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format("Order not found with id: %s", id));
    }

}
