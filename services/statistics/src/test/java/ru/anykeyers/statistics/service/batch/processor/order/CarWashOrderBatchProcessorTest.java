package ru.anykeyers.statistics.service.batch.processor.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.statistics.domain.OrderMetric;
import ru.anykeyers.statistics.repository.OrderMetricRepository;

import java.util.List;

/**
 * Тесты для {@link CarWashOrderBatchProcessor}
 */
@ExtendWith(MockitoExtension.class)
class CarWashOrderBatchProcessorTest {

    @Mock
    private OrderMetricRepository orderMetricRepository;

    @InjectMocks
    private CarWashOrderBatchProcessor carWashOrderBatchProcessor;

    /**
     * Тест выполнения задания на сохранение метрик по заказу
     */
    @Test
    void getProcessTask() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCarWashId(2L);

        carWashOrderBatchProcessor.getProcessTask(orderDTO).run();

        OrderMetric expectedMetric = new OrderMetric();
        expectedMetric.setCount(1L);
        expectedMetric.setCarWashId(2L);
        OrderMetric actualMetric = carWashOrderBatchProcessor.getMetric(2L);
        Assertions.assertEquals(expectedMetric.getCount(), actualMetric.getCount());
        Assertions.assertEquals(expectedMetric.getCarWashId(), actualMetric.getCarWashId());
    }

    /**
     * Тест выполнения задания на сохранение кэша метрик в БД
     */
    @Test
    void getBatchProcessTask() {
        OrderDTO firstOrder = new OrderDTO();
        firstOrder.setCarWashId(1L);
        OrderDTO secondOrder = new OrderDTO();
        secondOrder.setCarWashId(2L);
        for (OrderDTO order : List.of(firstOrder, secondOrder)) {
            carWashOrderBatchProcessor.getProcessTask(order).run();
        }
        carWashOrderBatchProcessor.getBatchProcessTask().run();
        Mockito.verify(orderMetricRepository).saveAll(Mockito.any());
    }

    /**
     * Тест выполнения задания на сохранение пустого кэша метрик в БД
     */
    @Test
    void getBatchProcessTaskWithEmptyCache() {
        carWashOrderBatchProcessor.getBatchProcessTask().run();
        Mockito.verify(orderMetricRepository, Mockito.never()).saveAll(Mockito.any());
    }

}