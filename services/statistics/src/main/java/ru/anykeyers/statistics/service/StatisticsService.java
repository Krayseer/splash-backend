package ru.anykeyers.statistics.service;

import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.statistics.domain.CarWashMetric;
import ru.anykeyers.statistics.domain.Statistics;

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
    Statistics getStatistics(Long carWashId);

    /**
     * Получить статистику по автомойке в формате PDF
     *
     * @param carWashId идентификатор автомойки
     */
    byte[] getStatisticsPdf(Long carWashId);

    /**
     * Собрать метрики по заказу
     *
     * @param orderDTO данные о заказе
     */
    void processOrder(OrderDTO orderDTO);

}
