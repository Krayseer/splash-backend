package ru.anykeyers.statistics.service;

import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.statistics.domain.CarWashMetric;

import java.util.List;

/**
 * Сервис статистики
 */
public interface StatisticsService {

    /**
     * Получить статистику по автомойке
     *
     * @param carWashId идентификатор автомойки
     */
    List<CarWashMetric> getStatistics(Long carWashId);

    /**
     * Собрать метрики по заказу
     *
     * @param orderDTO данные о заказе
     */
    void processOrder(OrderDTO orderDTO);

}
