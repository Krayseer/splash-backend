package ru.anykeyers.businessorderservice.processor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.anykeyers.businessorderservice.service.OrderService;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * Тесты для {@link AutoOrderProcessor}
 */
@ExtendWith(MockitoExtension.class)
class AutoOrderProcessorTest {

    @Mock
    private OrderService orderService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private AutoOrderProcessor processor;

    /**
     * Тест обработки заказа - назначения работника заказу
     */
    @Test
    void processOrder() {
        // Подготовка
        OrderDTO order = OrderDTO.builder().id(2L).build();
        UUID employeeId = UUID.randomUUID();
        Mockito.when(orderService.getFreeEmployees(order)).thenReturn(Set.of(employeeId));

        // Действие
        processor.processOrder(order);

        // Проверка
        Mockito.verify(orderService).appointOrderEmployee(order, employeeId);
        Mockito.verify(kafkaTemplate, Mockito.never()).send(Mockito.anyString(), Mockito.any());
    }

    /**
     * Тест обработки заказа с нехваткой свободных работников
     */
    @Test
    void processOrder_withEmptyFreeEmployees() {
        // Подготовка
        OrderDTO order = OrderDTO.builder().id(2L).build();
        Mockito.when(orderService.getFreeEmployees(order)).thenReturn(Collections.emptySet());

        // Действие
        processor.processOrder(order);

        // Проверка
        Mockito.verify(kafkaTemplate).send("order-delete", order);
        Mockito.verify(orderService, Mockito.never()).appointOrderEmployee(Mockito.anyLong(), Mockito.any());
    }

}