//package ru.anykeyers.orderservice.processor;
//
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageRequest;
//import ru.anykeyers.commonsapi.domain.order.OrderState;
//import ru.anykeyers.orderservice.repository.OrderRepository;
//import ru.anykeyers.orderservice.domain.Order;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.Executors;
//
///**
// * Тесты для класса {@link OrderStateProcessor}
// */
//@ExtendWith(MockitoExtension.class)
//public class OrderProcessorTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private OrderStateProcessor orderProcessor;
//
//    @BeforeEach
//    public void setUp() {
//        orderProcessor = new OrderStateProcessor(orderRepository, Executors.newVirtualThreadPerTaskExecutor());
//    }
//
//    /**
//     * Тест обработки заказов, которые ждут обработки
//     * 1) При наступлении времени заказа его статус меняется и обновляется в БД
//     * 2) Если время еще не наступило, то ничего не происходит
//     */
//    @Test
//    @SneakyThrows
//    void verifyProcessingOrders() {
//        Order processingOrder = Order.builder()
//                .status(OrderState.WAIT_PROCESS)
//                .startTime(Instant.now().minus(2, ChronoUnit.SECONDS))
//                .endTime(Instant.now().plus(35, ChronoUnit.MINUTES))
//                .build();
//        Order waitingOrder = Order.builder()
//                .status(OrderState.WAIT_PROCESS)
//                .startTime(Instant.now().plus(1, ChronoUnit.HOURS))
//                .endTime(Instant.now().plus(2, ChronoUnit.HOURS))
//                .build();
//
//        Mockito.when(orderRepository.findByStatus(OrderState.WAIT_PROCESS, PageRequest.of(0, 100)))
//                .thenReturn(List.of(processingOrder, waitingOrder));
//
//        orderProcessor.verifyOrders();
//        Thread.sleep(500);
//
//        Mockito.verify(orderRepository, Mockito.times(1)).save(processingOrder);
//        Assertions.assertEquals(OrderState.PROCESSING, processingOrder.getState());
//        Mockito.verify(orderRepository, Mockito.never()).save(waitingOrder);
//        Assertions.assertEquals(OrderState.WAIT_PROCESS, waitingOrder.getState());
//    }
//
//    /**
//     * Тест обработки заказов, которые уже были обработаны
//     * 1) Если наступило время завершения заказа, то его статус меняется и сохраняется в БД
//     * 2) Если время еще не наступило, то ничего не происходит
//     */
//    @Test
//    @SneakyThrows
//    void verifyProcessedOrders() {
//        Order processingOrder = Order.builder()
//                .status(OrderState.PROCESSING)
//                .startTime(Instant.now().minus(2, ChronoUnit.SECONDS))
//                .endTime(Instant.now().plus(30, ChronoUnit.MINUTES))
//                .build();
//        Order processedOrder = Order.builder()
//                .status(OrderState.PROCESSING)
//                .startTime(Instant.now().minus(1, ChronoUnit.HOURS))
//                .endTime(Instant.now().minus(30, ChronoUnit.SECONDS))
//                .build();
//
//        Mockito.when(orderRepository.findByStatus(OrderState.WAIT_PROCESS, PageRequest.of(0, 100)))
//                .thenReturn(Collections.emptyList());
//        Mockito.when(orderRepository.findByStatus(OrderState.PROCESSING, PageRequest.of(0, 100)))
//                .thenReturn(List.of(processingOrder, processedOrder));
//
//        orderProcessor.verifyOrders();
//        Thread.sleep(500);
//
//        Mockito.verify(orderRepository, Mockito.times(0)).save(processingOrder);
//        Assertions.assertEquals(OrderState.PROCESSING, processingOrder.getState());
//        Mockito.verify(orderRepository, Mockito.times(1)).save(processedOrder);
//        Assertions.assertEquals(OrderState.PROCESSED, processedOrder.getState());
//    }
//
//}