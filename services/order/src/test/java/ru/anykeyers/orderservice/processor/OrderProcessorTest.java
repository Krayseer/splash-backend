package ru.anykeyers.orderservice.processor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.service.OrderService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * Тесты для класса {@link OrderStateProcessor}
 */
@ExtendWith(MockitoExtension.class)
public class OrderProcessorTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderStateProcessor orderProcessor;

    /**
     * Тест обработки заказов, которые ждут обработки
     * 1) При наступлении времени заказа его статус меняется и обновляется в БД
     * 2) Если время еще не наступило, то ничего не происходит
     */
    @Test
    @SneakyThrows
    void verifyProcessingOrders() {
        Order processingOrder = Order.builder()
                .state(OrderState.WAIT_PROCESS)
                .startTime(Instant.now().minus(2, ChronoUnit.SECONDS).toEpochMilli())
                .endTime(Instant.now().plus(35, ChronoUnit.MINUTES).toEpochMilli())
                .build();
        Order waitingOrder = Order.builder()
                .state(OrderState.WAIT_PROCESS)
                .startTime(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())
                .endTime(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli())
                .build();

        Mockito.when(orderService.getOrdersByState(OrderState.PROCESSING))
                .thenReturn(Collections.emptyList());
        Mockito.when(orderService.getOrdersByState(OrderState.WAIT_PROCESS))
                .thenReturn(List.of(processingOrder, waitingOrder));

        orderProcessor.verifyOrders();
        Thread.sleep(1000);

        Mockito.verify(orderService, Mockito.never()).saveOrUpdate(processingOrder);
        Assertions.assertEquals(OrderState.WAIT_PROCESS, processingOrder.getState());
        Mockito.verify(orderService).saveOrUpdate(waitingOrder);
        Assertions.assertEquals(OrderState.PROCESSING, waitingOrder.getState());
    }

    /**
     * Тест обработки заказов, которые уже были обработаны
     * 1) Если наступило время завершения заказа, то его статус меняется и сохраняется в БД
     * 2) Если время еще не наступило, то ничего не происходит
     */
    @Test
    @SneakyThrows
    void verifyProcessedOrders() {
        Order processingOrder = Order.builder()
                .state(OrderState.PROCESSING)
                .startTime(Instant.now().minus(2, ChronoUnit.SECONDS).toEpochMilli())
                .endTime(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli())
                .build();
        Order processedOrder = Order.builder()
                .state(OrderState.PROCESSING)
                .startTime(Instant.now().minus(1, ChronoUnit.HOURS).toEpochMilli())
                .endTime(Instant.now().minus(30, ChronoUnit.SECONDS).toEpochMilli())
                .build();

        Mockito.when(orderService.getOrdersByState(OrderState.WAIT_PROCESS))
                .thenReturn(Collections.emptyList());
        Mockito.when(orderService.getOrdersByState(OrderState.PROCESSING))
                .thenReturn(List.of(processingOrder, processedOrder));

        orderProcessor.verifyOrders();
        Thread.sleep(1000);

        Mockito.verify(orderService).saveOrUpdate(processingOrder);
        Assertions.assertEquals(OrderState.PROCESSED, processingOrder.getState());
        Mockito.verify(orderService, Mockito.never()).saveOrUpdate(processedOrder);
        Assertions.assertEquals(OrderState.PROCESSING, processedOrder.getState());
    }

}