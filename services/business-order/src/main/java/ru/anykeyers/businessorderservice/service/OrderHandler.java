package ru.anykeyers.businessorderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.anykeyers.businessorderservice.processor.OrderProcessor;
import ru.anykeyers.businessorderservice.processor.OrderProcessorFactory;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationInfoDTO;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;

/**
 * Обработчик сообщений с Kafka
 */
@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = MessageQueue.ORDER_CREATE,
        groupId = OrderHandler.GROUP_ID
)
public class OrderHandler {

    public static final String GROUP_ID = "business-order-group";

    private final OrderProcessorFactory orderProcessorFactory;

    private final RemoteConfigurationService remoteConfigurationService;

    /**
     * Слушатель создания заказов
     */
    @KafkaHandler
    public void receiveOrderCreate(OrderDTO order) {
        ConfigurationInfoDTO configuration = remoteConfigurationService.getConfiguration(order.getCarWashId());
        OrderProcessor processor = orderProcessorFactory.getProcessor(configuration.getOrderProcessMode());
        if (processor == null) {
            throw new RuntimeException("No order process mode configured");
        }
        processor.processOrder(order);
    }

}
