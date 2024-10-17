package ru.anykeyers.businessorderservice.processor;

import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

/**
 * Обработчик заказа
 */
public interface OrderProcessor {

    /**
     * Обработать заказ
     */
    void processOrder(OrderDTO order);

    /**
     * Получить режим обработки заказа
     */
    OrderProcessMode getOrderProcessMode();

}
