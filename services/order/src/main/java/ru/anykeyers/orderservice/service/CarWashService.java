package ru.anykeyers.orderservice.service;

import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.domain.Order;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Сервис обработки заказов автомойки
 */
public interface CarWashService {

    /**
     * Получить список заказов автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    List<Order> getCarWashOrders(Long carWashId);

    /**
     * Получить список свободных интервалов времени для заказа
     *
     * @param carWashId идентификатор автомойки
     * @param date      дата
     */
    Set<Interval> getOrderFreeTimes(Long carWashId, Instant date);

    /**
     * Получить список заказов
     *
     * @param carWashId идентификатор автомойки
     * @param date      дата, за которую нужно получить список заказов
     */
    List<Order> findOrdersByDate(Long carWashId, Instant date);

    /**
     * Получить список заказов, ожидающих обработки
     *
     * @param carWashId идентификатор автомойки
     */
    List<Order> getWaitConfirmOrders(Long carWashId);

    /**
     * Получить количество заказов
     *
     * @param carWashId     идентификатор автомойки
     * @param orderState    статус заказа
     */
    int getOrdersCount(Long carWashId, OrderState orderState);
}
