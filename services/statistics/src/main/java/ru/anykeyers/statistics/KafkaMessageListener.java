package ru.anykeyers.statistics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.statistics.service.StatisticsService;

/**
 * Обработчик сообщений, поступающих по Kafka
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageListener {

    private static final String GROUP_ID = "statistics-group";

    private final ObjectMapper objectMapper;

    private final StatisticsService statisticsService;

     /**
     * Слушатель события создания заказа
     */
    @SneakyThrows
    @KafkaListener(topics = MessageQueue.ORDER_CREATE, groupId = GROUP_ID)
    public void receiveOrderCreate(String orderMessage) {
        OrderDTO order = objectMapper.readValue(orderMessage, new TypeReference<>() {});
        statisticsService.processOrder(order);
    }

}
