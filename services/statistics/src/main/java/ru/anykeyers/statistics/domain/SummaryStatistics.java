package ru.anykeyers.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryStatistics {

    /**
     * Кол-во всех выполненных услуг
     */
    private long summaryCount;

    /**
     * Сумма всех выполненных услуг
     */
    private long summaryPrice;

    /**
     * Статистика по услугам
     */
    private List<ServiceStatistics> services;

}
