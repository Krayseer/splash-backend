package ru.anykeyers.orderservice.service;

import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.web.dto.OrderCreateRequest;

import java.util.List;

/**
 * Сервис обработки заказов для пользователей
 */
public interface UserOrderService {

    /**
     * Получить список активных заказов
     *
     * @param user пользователь
     */
    List<Order> getActiveOrders(User user);

    /**
     * Получить список завершенных заказов
     *
     * @param username имя пользователя
     */
    List<Order> getProcessedOrders(User username);

    /**
     * Создать заказ
     *
     * @param username      имя пользователя
     * @param createRequest данные о заказе
     *
     * @return обработанный заказ
     */
    Order createOrder(User username, OrderCreateRequest createRequest);

    /**
     * Удалить заказ
     *
     * @param orderId   идентификатор заказа
     */
    void deleteOrder(Long orderId);

}
