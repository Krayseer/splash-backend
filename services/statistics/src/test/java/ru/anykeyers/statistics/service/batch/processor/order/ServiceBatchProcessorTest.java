package ru.anykeyers.statistics.service.batch.processor.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.statistics.domain.ServiceMetric;
import ru.anykeyers.statistics.repository.ServiceMetricRepository;

import java.util.List;

/**
 * Тесты для {@link ServiceBatchProcessor}
 */
@ExtendWith(MockitoExtension.class)
class ServiceBatchProcessorTest {

    @Mock
    private ServiceMetricRepository serviceMetricRepository;

    @InjectMocks
    private ServiceBatchProcessor serviceBatchProcessor;

    /**
     * Тест выполнения задания на сохранение метрик по услугам заказа
     */
    @Test
    void getProcessTask() {
        List<ServiceDTO> services = List.of(
                new ServiceDTO(1L, "service1", 2000, 250),
                new ServiceDTO(2L, "service2", 3000, 500)
        );
        OrderDTO orderDTO = OrderDTO.builder().services(services).build();
        orderDTO.setCarWashId(2L);

        serviceBatchProcessor.getProcessTask(orderDTO).run();

        ServiceMetric expectedMetric = new ServiceMetric();
        expectedMetric.setCarWashId(2L);
        expectedMetric.setCount(2L);
        expectedMetric.setSum(750);
        ServiceMetric actualMetric = serviceBatchProcessor.getMetric(2L);
        Assertions.assertEquals(expectedMetric.getCarWashId(), actualMetric.getCarWashId());
        Assertions.assertEquals(expectedMetric.getCount(), actualMetric.getCount());
        Assertions.assertEquals(expectedMetric.getSum(), actualMetric.getSum());
    }

}