package ru.anykeyers.businessorderservice.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.anykeyers.businessorderservice.service.OrderService;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Автоматическая обработка заказа
 */
@Component
@RequiredArgsConstructor
public class AutoOrderProcessor implements OrderProcessor {

    private final OrderService orderService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void processOrder(OrderDTO order) {
        Set<UUID> freeEmployees = orderService.getFreeEmployees(order);
        if (freeEmployees.isEmpty()) {
            kafkaTemplate.send(MessageQueue.ORDER_DELETE, order);
            return;
        }
        orderService.appointOrderEmployee(order, getFreeEmployeeOrder(freeEmployees));
    }

    @Override
    public OrderProcessMode getOrderProcessMode() {
        return OrderProcessMode.AUTO;
    }

    private UUID getFreeEmployeeOrder(Set<UUID> freeEmployees) {
        return new ArrayList<>(freeEmployees).getFirst();
    }

}
