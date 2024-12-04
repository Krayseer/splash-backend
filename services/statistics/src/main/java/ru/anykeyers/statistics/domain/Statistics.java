package ru.anykeyers.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Статистика
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {

    /**
     * Общая статистика за все время
     */
    private SummaryStatistics summaryStatistics;

    /**
     * Статистика услуг по датам
     */
    private List<DateStatistics> dateStatistics;

}
