package ru.anykeyers.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Статистика услуги
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatistics {

    /**
     * Название услуги
     */
    private String name;

    /**
     * Сумма
     */
    private long sum;

    /**
     * Количество
     */
    private long count;

}
