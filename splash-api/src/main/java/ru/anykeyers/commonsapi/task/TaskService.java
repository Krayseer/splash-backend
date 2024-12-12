package ru.anykeyers.commonsapi.task;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Сервис обработки заданий
 */
@Slf4j
@EnableScheduling
public class TaskService {

    /**
     * Интервал обработки задач
     */
    private static final int PROCESS_DELAY_MS = 1000 * 5;

    /**
     * Интервал очистки завершённых задач
     */
    private static final int CLEANUP_DELAY_MS = 1000 * 60 * 2;

    /**
     * Задачи пользователей
     */
    private final ConcurrentMap<UUID, List<Task>> tasks = new ConcurrentHashMap<>();

    /**
     * Пул выполнения заданий
     */
    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Добавить новое задание на выполнение.
     *
     * @param task задание
     */
    public void addTask(Task task) {
        tasks.computeIfAbsent(task.getUserId(), k -> new CopyOnWriteArrayList<>()).add(task);
    }

    /**
     * @return список всех заданий пользователя.
     */
    public List<Task> getTasks(UUID userId) {
        return new ArrayList<>(tasks.getOrDefault(userId, new ArrayList<>()));
    }

    /**
     * Периодический процесс выполнения задач.
     */
    @Scheduled(fixedRate = PROCESS_DELAY_MS)
    private void processTasks() {
        if (!hasTasksForProcessing()) {
            return;
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<Future<?>> futures = new ArrayList<>();
        for (Task task : getAllTasks()) {
            if (task.getTaskState() == TaskState.COMPLETE) {
                continue; // Пропускаем завершённые задачи
            }
            futures.add(threadPool.submit(task));
        }

        // Ожидаем завершения выполнения задач
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Task execution error: ", e);
            }
        });

        stopWatch.stop();
        log.info("Processed {} background tasks in {} ms", futures.size(), stopWatch.getTotalTimeMillis());
    }

    /**
     * Шедулер для удаления завершённых задач каждые 2 минуты.
     */
    @Scheduled(fixedRate = CLEANUP_DELAY_MS)
    private void cleanupCompletedTasks() {
        for (UUID userId : tasks.keySet()) {
            List<Task> userTasks = tasks.get(userId);
            if (userTasks == null || userTasks.isEmpty()) {
                continue;
            }

            // Фильтруем задачи, оставляя только незавершённые
            List<Task> remainingTasks = userTasks.stream()
                    .filter(task -> task.getTaskState() != TaskState.COMPLETE)
                    .collect(Collectors.toList());

            if (remainingTasks.isEmpty()) {
                tasks.remove(userId); // Удаляем пользователя, если у него нет активных задач
            } else {
                tasks.put(userId, remainingTasks); // Обновляем список задач
            }
        }
        log.info("Cleanup completed tasks. Remaining tasks: {}", tasks.size());
    }

    /**
     * @return список всех задач.
     */
    private List<Task> getAllTasks() {
        return tasks.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private boolean hasTasksForProcessing() {
        List<Task> allTasks = getAllTasks();
        return allTasks.stream().anyMatch(x -> x.getTaskState() != TaskState.COMPLETE);
    }

    @PreDestroy
    public void shutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

}

