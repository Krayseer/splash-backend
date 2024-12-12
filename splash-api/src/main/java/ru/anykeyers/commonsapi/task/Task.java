package ru.anykeyers.commonsapi.task;

import java.util.UUID;

/**
 * Задание
 */
public interface Task extends Runnable {

    /**
     * @return название задания
     */
    String getName();

    /**
     * @return состояние выполнения задания
     */
    TaskState getTaskState();

    /**
     * @return идентификатор пользователя
     */
    UUID getUserId();

    /**
     * @return процент выполнения задания
     */
    int getPercentage();

}
