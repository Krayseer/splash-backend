package ru.anykeyers.statistics.service.batch.processor.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.anykeyers.statistics.domain.CarWashMetric;
import ru.anykeyers.statistics.repository.CarWashMetricRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Базовый класс пакетных обработчиков заказов
 * @param <T>
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseOrderBatchProcessor<T extends CarWashMetric> implements OrderBatchProcessor {

    private final Map<Long, T> metricByCarWashId = new ConcurrentHashMap<>();

    private final CarWashMetricRepository<T> carWashMetricRepository;

    private final Supplier<T> carWashMetricCreator;

    protected T getMetric(Long carWashId) {
        if (!metricByCarWashId.containsKey(carWashId)) {
            T metric = carWashMetricRepository.findByCarWashId(carWashId);
            if (metric == null) {
                metric = carWashMetricCreator.get();
                metric.setCarWashId(carWashId);
            }
            metricByCarWashId.put(carWashId, metric);
        }
        return metricByCarWashId.get(carWashId);
    }

    @Override
    public Runnable getBatchProcessTask() {
        return () -> {
            if (metricByCarWashId.isEmpty()) {
                return;
            }
            log.info("Save order batch in DB: {}", metricByCarWashId.size());
            carWashMetricRepository.saveAll(metricByCarWashId.values());
            metricByCarWashId.clear();
        };
    }

}
