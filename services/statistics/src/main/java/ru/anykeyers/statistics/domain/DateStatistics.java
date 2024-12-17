package ru.anykeyers.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Статистика по дате
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateStatistics {

    /**
     * Дата
     */
    private String date;

    /**
     * Количество заказов
     */
    private long ordersCount;

    /**
     * Количество выполненных услуг
     */
    private long serviceCountSummary;

    /**
     * Сумма выполненных услуг
     */
    private long servicePriceSummary;

}
