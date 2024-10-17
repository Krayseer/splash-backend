package ru.anykeyers.statistics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.statistics.domain.CarWashMetric;
import ru.anykeyers.statistics.repository.CarWashMetricRepository;
import ru.anykeyers.statistics.service.batch.processor.order.OrderBatchProcessor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Реализация сервиса статистики
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ExecutorService executorService;

    private final List<OrderBatchProcessor> orderBatchProcessors;

    private final List<CarWashMetricRepository<?>> carWashMetricRepositories;

    @Override
    public List<CarWashMetric> getStatistics(Long carWashId) {
        return carWashMetricRepositories.stream()
                .map(carWashMetricRepository -> carWashMetricRepository.findByCarWashId(carWashId))
                .collect(Collectors.toList());
    }

    @Override
    public void processOrder(OrderDTO orderDTO) {
        log.info("Processing order {}", orderDTO);
        for (OrderBatchProcessor processor : orderBatchProcessors) {
            executorService.execute(processor.getProcessTask(orderDTO));
        }
    }

}
