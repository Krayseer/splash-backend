package ru.anykeyers.commonsapi.domain.order;

/**
 * Состояние заказа
 */
public enum OrderState {
    /**
     * Ожидает одобрения
     */
    WAIT_CONFIRM,
    /**
     * Ожидает обработки
     */
    WAIT_PROCESS,
    /**
     * Находится в обработке
     */
    PROCESSING,
    /**
     * Обработан
     */
    PROCESSED
}
