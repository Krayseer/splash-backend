package ru.anykeyers.orderservice.service;

import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.domain.Order;

import java.util.List;


/**
 * Сервис обработки заказов
 */
public interface OrderService extends UserOrderService, CarWashService {

    /**
     * Получить информацию о заказе
     *
     * @param id идентификатор заказа
     */
    Order getOrder(Long id);

    /**
     * Получить информацию о списке заказов
     *
     * @param orderIds идентификаторы заказов
     */
    List<Order> getOrders(List<Long> orderIds);

    /**
     * Получить список заказов
     *
     * @param orderState статус заказа
     */
    List<Order> getOrders(OrderState orderState);

    /**
     * Сохранить заказ
     *
     * @param order заказ
     */
    void saveOrUpdate(Order order);

}
