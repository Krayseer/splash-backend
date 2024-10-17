package ru.anykeyers.businessorderservice.processor;

import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

/**
 * Обработка заказа менеджером
 */
@Component
public class ManagementOrderProcessor implements OrderProcessor {

    @Override
    public void processOrder(OrderDTO order) {
    }

    @Override
    public OrderProcessMode getOrderProcessMode() {
        return OrderProcessMode.MANAGER_PROCESSING;
    }

}
