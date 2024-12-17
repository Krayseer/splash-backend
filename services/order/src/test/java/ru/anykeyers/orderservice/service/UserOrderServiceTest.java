package ru.anykeyers.orderservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.domain.PaymentType;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.orderservice.calculator.BoxCalculator;
import ru.anykeyers.orderservice.calculator.OrderCalculator;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.domain.exception.BoxFreeNotFoundException;
import ru.anykeyers.orderservice.repository.OrderRepository;
import ru.anykeyers.orderservice.service.impl.UserOrderServiceImpl;
import ru.anykeyers.orderservice.web.dto.OrderCreateRequest;
import ru.anykeyers.orderservice.web.mapper.OrderMapper;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Тесты для {@link UserOrderService}
 */
@ExtendWith(MockitoExtension.class)
class UserOrderServiceTest {

    @Mock
    private BoxCalculator boxCalculator;

    @Mock
    private OrderCalculator orderCalculator;

    @Mock
    private OrderRepository repository;

    @Mock
    private CarWashOrderService carWashOrderService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private UserOrderServiceImpl orderService;

    @Captor
    private ArgumentCaptor<Order> captor;

    /**
     * Тест создания заказа
     */
    @Test
    void createOrder() {
        // Подготовка
        Long boxId = 1L;
        Long carWashId = 1L;
        long serviceDuration = 2000L;
        List<Long> serviceIds = List.of(1L, 2L);
        long startTime = 500L;
        OrderCreateRequest orderRequest = new OrderCreateRequest(carWashId, serviceIds, PaymentType.SBP, startTime);
        List<Order> carWashOrders = Collections.emptyList();
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        Mockito.when(orderCalculator.calculateOrderEndTime(startTime, serviceIds)).thenReturn(serviceDuration);
        Mockito.when(carWashOrderService.getCarWashOrders(carWashId)).thenReturn(carWashOrders);
        Mockito.when(boxCalculator.findFreeBox(carWashId, carWashOrders, Interval.of(500, 2000))).thenReturn(boxId);

        // Действие
        orderService.createOrder(user, orderRequest);

        // Проверка
        Mockito.verify(repository).save(captor.capture());
        Order savedOrder = captor.getValue();
        Assertions.assertEquals(userId, savedOrder.getUserId());
        Assertions.assertEquals(PaymentType.SBP, savedOrder.getTypePayment());
        Assertions.assertEquals(startTime, savedOrder.getStartTime());
        Assertions.assertEquals(boxId, savedOrder.getBoxId());
        Assertions.assertEquals(serviceIds, savedOrder.getServiceIds());
        Assertions.assertEquals(OrderState.WAIT_CONFIRM, savedOrder.getState());

        Mockito.verify(kafkaTemplate).send(Mockito.eq("order-create"), Mockito.any());
    }

    /**
     * Тест создания заказа, если не нашлось свободных боксов
     */
    @Test
    void createOrder_notFoundFreeBox() {
        // Подготовка
        OrderCreateRequest orderRequest = new OrderCreateRequest(
                1L, List.of(1L, 2L), PaymentType.SBP, Instant.now().toEpochMilli()
        );
        Mockito.when(boxCalculator.findFreeBox(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(BoxFreeNotFoundException.class);

        // Действие
        Assertions.assertThrows(
                BoxFreeNotFoundException.class, () -> orderService.createOrder(User.builder().id(UUID.randomUUID()).build(), orderRequest)
        );

        // Проверка
        Mockito.verify(kafkaTemplate, Mockito.times(0)).send(Mockito.any(), Mockito.any());
        Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    }

}