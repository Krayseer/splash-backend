package ru.anykeyers.commonsapi.domain.configuration;

/**
 * Режим обработки заказов
 */
public enum OrderProcessMode {
    /**
     * Автоматическая обработка
     */
    AUTO,
    /**
     * Самообслуживание
     */
    SELF_SERVICE,
    /**
     * Обработка менеджером
     */
    MANAGER_PROCESSING
}
