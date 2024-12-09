package ru.anykeyers.orderservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.domain.exception.OrderNotFoundException;
import ru.anykeyers.orderservice.service.impl.EmployeeServiceImpl;

/**
 * Тесты для {@link EmployeeService}
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Captor
    private ArgumentCaptor<Order> captor;

    /**
     * Проверка назначения работника на заказ - установка статуса заказа в "Ожидает обработки"
     */
    @Test
    void applyOrderEmployee() {
        // Подготовка
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .state(OrderState.WAIT_CONFIRM)
                .startTime(200)
                .endTime(750)
                .boxId(2L)
                .build();
        Mockito.when(orderService.getOrder(orderId)).thenReturn(order);

        // Действие
        employeeService.applyOrderEmployee(orderId);

        // Проверка
        Mockito.verify(orderService).saveOrUpdate(captor.capture());
        Assertions.assertEquals(OrderState.WAIT_PROCESS, captor.getValue().getState());
    }

    /**
     * Тест обработки назначения работника несуществующему заказу
     */
    @Test
    void applyOrderEmployee_notExistsOrder() {
        // Подготовка
        Mockito.when(orderService.getOrder(1L)).thenThrow(OrderNotFoundException.class);

        // Действие
        Assertions.assertThrows(
                OrderNotFoundException.class, () -> employeeService.applyOrderEmployee(1L)
        );

        // Проверка
        Mockito.verify(orderService, Mockito.never()).saveOrUpdate(Mockito.any());
    }

    /**
     * Тест удаления работника с несуществующего заказа
     */
    @Test
    void disappointEmployeeFromNotExistsOrder() {
        // Подготовка
        Mockito.when(orderService.getOrder(1L)).thenThrow(OrderNotFoundException.class);

        // Действие
        Assertions.assertThrows(
                OrderNotFoundException.class, () -> employeeService.disappointEmployeeFromOrder(1L)
        );

        // Проверка
        Mockito.verify(orderService, Mockito.never()).saveOrUpdate(Mockito.any());
    }

    /**
     * Проверка удаления работника с заказа - установки статуса заказа в "Ожидает одобрения"
     */
    @Test
    void disappointEmployeeFromOrder() {
        // Подготовка
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .state(OrderState.WAIT_PROCESS)
                .startTime(200)
                .endTime(750)
                .boxId(2L)
                .build();
        Mockito.when(orderService.getOrder(orderId)).thenReturn(order);

        // Действие
        employeeService.disappointEmployeeFromOrder(orderId);

        // Проверка
        Mockito.verify(orderService).saveOrUpdate(captor.capture());
        Assertions.assertEquals(OrderState.WAIT_CONFIRM, captor.getValue().getState());
    }

}