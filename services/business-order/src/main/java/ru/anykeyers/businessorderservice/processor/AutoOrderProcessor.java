package ru.anykeyers.businessorderservice.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.anykeyers.businessorderservice.service.OrderService;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

import java.util.List;
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
        List<UUID> freeEmployees = orderService.getFreeEmployees(order);
        if (freeEmployees.isEmpty()) {
            kafkaTemplate.send(MessageQueue.ORDER_DELETE, order);
            return;
        }
        orderService.appointOrderEmployee(order.getId(), getFreeEmployeeOrder(freeEmployees));
        kafkaTemplate.send(MessageQueue.EMPLOYEE_ORDER_APPLY, order);
    }

    @Override
    public OrderProcessMode getOrderProcessMode() {
        return OrderProcessMode.AUTO;
    }

    private UUID getFreeEmployeeOrder(List<UUID> freeEmployees) {
        return freeEmployees.getFirst();
    }

}
