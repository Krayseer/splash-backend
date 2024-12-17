package ru.anykeyers.statistics.service;

import ru.anykeyers.statistics.domain.Statistics;

/**
 * Генератор отчетов
 */
public interface ReportGenerator {

    /**
     * Сгенерировать отчет по статистике
     *
     * @param statistics статистика
     */
    byte[] generate(Statistics statistics);

}
