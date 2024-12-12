package ru.anykeyers.commonsapi.task;

/**
 * Состояние задания
 */
public enum TaskState {
    /**
     * В ожидании
     */
    PENDING,
    /**
     * В обработке
     */
    IN_PROCESS,
    /**
     * Ошибка выполнения
     */
    FAILED,
    /**
     * Выполнен
     */
    COMPLETE
}
