package ru.anykeyers.statistics.service.batch.processor.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.statistics.domain.ServiceMetric;
import ru.anykeyers.statistics.repository.ServiceMetricRepository;

import java.util.List;

/**
 * Пакетная обработка услуг заказов
 */
@Slf4j
@Component
public class ServiceBatchProcessor extends BaseOrderBatchProcessor<ServiceMetric> {

    public ServiceBatchProcessor(ServiceMetricRepository serviceMetricRepository) {
        super(serviceMetricRepository, ServiceMetric::new);
    }

    @Override
    public Runnable getProcessTask(OrderDTO order) {
        return () -> {
            ServiceMetric serviceMetric = getMetric(order.getCarWashId());
            List<ServiceDTO> services = order.getServices();
            serviceMetric.addSum(services.stream().mapToInt(ServiceDTO::getPrice).sum());
            serviceMetric.addCount(services.size());
        };
    }

}
