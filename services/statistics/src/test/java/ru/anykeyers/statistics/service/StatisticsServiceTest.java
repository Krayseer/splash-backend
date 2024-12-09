package ru.anykeyers.statistics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.statistics.domain.CarWashMetric;
import ru.anykeyers.statistics.domain.key.CarWashMetricId;
import ru.anykeyers.statistics.domain.key.KeyGenerator;
import ru.anykeyers.statistics.repository.MetricRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Тесты для {@link StatisticsService}
 */
@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private MetricRepository metricRepository;

    private final KeyGenerator keyGenerator = new KeyGenerator();

    private StatisticsServiceImpl statisticsService;

    private final Long carWashId = 1L;
    private final LocalDate date = LocalDate.of(1973, 3, 3);
    private final CarWashMetricId metricId = new CarWashMetricId(carWashId, date);
    private final OrderDTO orderDTO = OrderDTO.builder()
            .id(123L)
            .carWashId(carWashId)
            .services(List.of(
                    new ServiceDTO(1L, "test1", 1000, 100),
                    new ServiceDTO(2L, "test2", 2000, 200)
            ))
            .createdAt(Instant.ofEpochMilli(100000000000L).toString())
            .build();

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsServiceImpl(metricRepository, keyGenerator, new RemoteServicesService() {
            @Override
            public List<ServiceDTO> getServices(Long carWashId) {
                return List.of();
            }

            @Override
            public List<ServiceDTO> getServices(List<Long> serviceIds) {
                return List.of();
            }

            @Override
            public long getServicesDuration(List<Long> serviceIds) {
                return 0;
            }
        });
    }

    /**
     * Создание новой метрики
     */
    @Test
    void processOrder_createMetric() {
        // Подготовка
        Mockito.when(metricRepository.findById(metricId)).thenReturn(Optional.empty());
        CarWashMetric newMetric = new CarWashMetric(metricId, 0L, new HashMap<>());
        Mockito.when(metricRepository.save(Mockito.any(CarWashMetric.class))).thenReturn(newMetric);

        // Действие
        statisticsService.processOrder(orderDTO);

        // Проверка
        Mockito.verify(metricRepository, Mockito.times(1)).findById(metricId);
        Mockito.verify(metricRepository, Mockito.times(1)).save(Mockito.argThat(metric -> {
            Assertions.assertEquals(1L, metric.getOrdersCount());
            Assertions.assertEquals(100L, metric.getCountByServiceId().get(1L));
            Assertions.assertEquals(200L, metric.getCountByServiceId().get(2L));
            return true;
        }));
    }

    /**
     * Обновление существующей метрики
     */
    @Test
    void processOrder_updateExistingMetric() {
        // Подготовка
        Map<Long, Long> metrics = new HashMap<>();
        metrics.put(1L, 50L);
        CarWashMetric existingMetric = new CarWashMetric(metricId, 1L, metrics);

        Mockito.when(metricRepository.findById(metricId)).thenReturn(Optional.of(existingMetric));

        // Действие
        statisticsService.processOrder(orderDTO);

        // Проверка
        Assertions.assertEquals(2L, existingMetric.getOrdersCount());
        Assertions.assertEquals(150L, existingMetric.getCountByServiceId().get(1L));
        Assertions.assertEquals(200L, existingMetric.getCountByServiceId().get(2L));
        Mockito.verify(metricRepository, Mockito.times(1)).save(existingMetric);
    }

}