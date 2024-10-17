package ru.anykeyers.configurationservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Асинхронный обработчик заданий
 */
@Component
public class TaskManager {

    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

    private final Queue<Runnable> failedTasks = new LinkedBlockingDeque<>();

    /**
     * Добавить задание на обработку
     */
    public void addTask(Runnable task) {
        try {
            threadPool.execute(task);
        } catch (Throwable ex){
            failedTasks.add(task);
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void rerunFailedTasks() {
        while (!failedTasks.isEmpty()) {
            addTask(failedTasks.poll());
        }
    }

}
