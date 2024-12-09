package ru.anykeyers.businessorderservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.anykeyers.businessorderservice.repository.BusinessOrderRepository;
import ru.anykeyers.businessorderservice.domain.BusinessOrder;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Тесты для {@link OrderService}
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private BusinessOrderRepository businessOrderRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    /**
     * Тест назначения работника заказу
     */
    @Test
    void appointOrderEmployee() {
        // Подготовка
        UUID employeeId = UUID.randomUUID();
        OrderDTO order = OrderDTO.builder().id(2L).build();

        // Действие
        orderService.appointOrderEmployee(order, employeeId);

        // Проверка
        Mockito.verify(businessOrderRepository).save(Mockito.any());
        Mockito.verify(kafkaTemplate).send("order-employee-apply", order);
    }

    /**
     * Тест успешного удаления работника с бизнес заказа
     */
    @Test
    void disappointEmployeeFromOrder() {
        // Подготовка
        UUID employeeId = UUID.randomUUID();
        BusinessOrder order = new BusinessOrder(1L, 2L, employeeId);
        Mockito.when(businessOrderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Действие
        orderService.disappointEmployeeFromOrder(1L);

        // Проверка
        Mockito.verify(businessOrderRepository).deleteById(1L);
        Mockito.verify(kafkaTemplate).send("order-employee-disappointment", 2L);
    }

    /**
     * Тест удаления работника с несуществующего бизнес заказа
     */
    @Test
    void disappointEmployee_notExistsBusinessOrder() {
        // Подготовка
        Mockito.when(businessOrderRepository.findById(1L)).thenReturn(Optional.empty());

        // Действие
        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class, () -> orderService.disappointEmployeeFromOrder(1L)
        );

        // Проверка
        Assertions.assertEquals("Order not found", exception.getMessage());
        Mockito.verify(businessOrderRepository, Mockito.never()).deleteById(1L);
        Mockito.verify(kafkaTemplate, Mockito.never()).send("order-employee-disappointment", 1L);
    }

}