package ru.anykeyers.orderservice.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.service.OrderService;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Обработчик, проверяющий состояние заказов
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStateProcessor {

    /**
     * Периодичность выполнения обработки
     */
    private final static long DELAY = 10 * 6000;

    private final OrderService orderService;

    private final ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Обработка состояний каждую минуту
     */
    @Scheduled(fixedDelay = DELAY)
    public void verifyOrders() {
        long now = Instant.now().toEpochMilli();
        threadPool.execute(() -> verifyProcessingOrders(now));
        threadPool.execute(() -> verifyProcessedOrders(now));
    }

    /**
     * Проверка заказов, находящихся в обработке
     */
    private void verifyProcessingOrders(long time) {
        verify(OrderState.WAIT_PROCESS, order -> {
            if (order.getStartTime() >= time && time <= order.getEndTime()) {
                order.setState(OrderState.PROCESSING);
                orderService.saveOrUpdate(order);
            }
        });
    }

    /**
     * Проверка завершенных заказов
     */
    private void verifyProcessedOrders(long time) {
        verify(OrderState.PROCESSING, order -> {
            if (order.getEndTime() >= time) {
                order.setState(OrderState.PROCESSED);
                orderService.saveOrUpdate(order);
            }
        });
    }

    /**
     * Обработка заказа
     *
     * @param orderState    статус заказа
     * @param consumer      обработчик
     */
    private void verify(OrderState orderState, Consumer<Order> consumer) {
        List<Order> orders = orderService.getOrdersByState(orderState);
        if (orders.isEmpty()) {
            return;
        }
        orders.forEach(consumer);
    }

}
