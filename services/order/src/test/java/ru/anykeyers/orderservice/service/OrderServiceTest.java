package ru.anykeyers.orderservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.repository.OrderRepository;
import ru.anykeyers.orderservice.service.impl.OrderServiceImpl;

/**
 * Тесты для {@link OrderService}
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderServiceImpl orderService;

    /**
     * Тест сохранения или обновления заказа
     */
    @Test
    void saveOrUpdate() {
        // Действие
        orderService.saveOrUpdate(Mockito.mock(Order.class));

        // Проверка
        Mockito.verify(repository).save(Mockito.any(Order.class));
    }

    /**
     * Тест удаления заказа
     */
    @Test
    void deleteOrder() {
        // Действие
        orderService.deleteOrder(1L);

        // Проверка
        Mockito.verify(repository).deleteById(1L);
    }
}