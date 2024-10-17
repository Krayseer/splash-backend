package ru.anykeyers.statistics.service.batch;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.anykeyers.statistics.service.batch.processor.BatchProcessor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Воркер по обработке пакетных обработчиков
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BatchWorker {

    private final ExecutorService executorService;

    private final List<BatchProcessor> batchProcessors;

    /**
     * Обработать все пакетные обработчики
     */
    @Scheduled(fixedRate = 10000)
    public void processBatches() {
        for (BatchProcessor batchProcessor : batchProcessors) {
            executorService.execute(batchProcessor.getBatchProcessTask());
        }
    }

    /**
     * Завершение работы ExecutorService при завершении работы приложения
     */
    @PreDestroy
    public void shutdownExecutor() {
        try {
            log.info("Shutting down executor service.");
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                log.warn("Executor did not terminate in the specified time.");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                log.warn("Executor was abruptly shut down. {} tasks will not be executed.", droppedTasks.size());
            }
        } catch (InterruptedException e) {
            log.error("Executor shutdown interrupted.", e);
            executorService.shutdownNow();
        }
    }

}
