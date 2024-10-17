package ru.anykeyers.statistics.service.batch.processor;

/**
 * Пакетный обработчик
 */
public interface BatchProcessor {

    /**
     * Получить задание на обработку пакеты
     */
    Runnable getBatchProcessTask();

}
