package ru.anykeyers.businessorderservice.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

/**
 * Обрабокта заказов в режиме самообслуживания
 */
@Component
@RequiredArgsConstructor
public class SelfOrderProcessor implements OrderProcessor {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void processOrder(OrderDTO order) {
        kafkaTemplate.send(MessageQueue.EMPLOYEE_ORDER_APPLY, order);
    }

    @Override
    public OrderProcessMode getOrderProcessMode() {
        return OrderProcessMode.SELF_SERVICE;
    }
}
