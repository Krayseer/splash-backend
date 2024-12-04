package ru.anykeyers.statistics.domain.key;

import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Генератор ключей для идентификатора метрики автомойки
 */
@Component
public class KeyGenerator {

    public CarWashMetricId generate(OrderDTO order) {
        LocalDate date = toLocalDate(Instant.parse(order.getCreatedAt()));
        return new CarWashMetricId(order.getCarWashId(), date);
    }

    private LocalDate toLocalDate(Instant instant) {
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDate();
    }

}
