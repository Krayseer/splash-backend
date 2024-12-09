package ru.anykeyers.orderservice.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.domain.exception.BoxFreeNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Калькулятор боксов
 */
@Component
@RequiredArgsConstructor
public class BoxCalculator {

    private final RemoteConfigurationService remoteConfigurationService;

    /**
     * Найти свободный бокс
     *
     * @param existingOrders    актуальные заказы
     * @param interval          интервал, на который нужно найти свободный бокс
     */
    public Long findFreeBox(Long carWashId, List<Order> existingOrders, Interval interval) {
        if (CollectionUtils.isEmpty(existingOrders)) {
            ConfigurationDTO configurationDTO = remoteConfigurationService.getConfiguration(carWashId);
            return configurationDTO.getBoxes().getFirst().getId();
        }
        Map<Long, List<Order>> ordersByBoxId = existingOrders.stream().collect(Collectors.groupingBy(Order::getBoxId));
        return ordersByBoxId.entrySet().stream()
                .filter(entry -> entry.getValue().stream()
                        .noneMatch(x -> isIntersectsOrEnters(interval, Interval.of(x.getStartTime(), x.getEndTime()))))
                .findFirst()
                .orElseThrow(BoxFreeNotFoundException::new)
                .getKey();
    }

    /**
     * Входит или пересекается ли интервал i1 с i2
     */
    private boolean isIntersectsOrEnters(Interval i1, Interval i2) {
        boolean intersects = i1.isInRange(i2.getStart()) || i1.isInRange(i2.getEnd());
        boolean enters = i1.getStart() >= i2.getStart() && i1.getEnd() <= i2.getEnd();
        return intersects || enters;
    }

}
