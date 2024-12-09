package ru.anykeyers.businessorderservice.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

/**
 * Тесты для {@link SelfOrderProcessor}
 */
@ExtendWith(MockitoExtension.class)
class SelfOrderProcessorTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private SelfOrderProcessor processor;

    /**
     * Тест обработки заказа
     */
    @Test
    void processOrder() {
        // Подготовка
        OrderDTO order = OrderDTO.builder().id(2L).build();

        // Действие
        processor.processOrder(order);

        // Проверка
        Mockito.verify(kafkaTemplate).send("order-employee-apply", order);
    }

}