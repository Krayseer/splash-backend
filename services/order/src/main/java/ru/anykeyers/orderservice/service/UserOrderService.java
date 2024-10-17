package ru.anykeyers.orderservice.service;

import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.orderservice.domain.Order;

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
    List<Order> getProcessedOrders(String username);

    /**
     * Создать заказ
     *
     * @param username  имя пользователя
     * @param orderDTO  данные о заказе
     *
     * @return обработанный заказ
     */
    Order createOrder(String username, OrderDTO orderDTO);

    /**
     * Удалить заказ
     *
     * @param orderId   идентификатор заказа
     */
    void deleteOrder(Long orderId);

}
