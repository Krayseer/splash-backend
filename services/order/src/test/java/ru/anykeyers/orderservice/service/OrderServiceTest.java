//package ru.anykeyers.orderservice.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.commonsapi.domain.order.OrderState;
//import ru.anykeyers.commonsapi.domain.PaymentType;
//import ru.anykeyers.commonsapi.domain.order.OrderDTO;
//import ru.anykeyers.commonsapi.remote.RemoteServicesService;
//import ru.anykeyers.orderservice.repository.OrderRepository;
//import ru.anykeyers.orderservice.domain.Order;
//import ru.anykeyers.orderservice.domain.OrderRequest;
//import ru.anykeyers.commonsapi.domain.Interval;
//import ru.anykeyers.orderservice.exception.BoxFreeNotFoundException;
//import ru.anykeyers.orderservice.exception.OrderNotFoundException;
//import ru.anykeyers.orderservice.service.impl.OrderServiceImpl;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
///**
// * Тесты для {@link OrderService}
// */
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//
//    @Mock
//    private BoxService boxService;
//
//    @Mock
//    private EventService eventService;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private RemoteServicesService remoteServicesService;
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    /**
//     * Тест успешного создания заказа
//     */
//    @Test
//    void saveOrder() {
//        String username = "test-user";
//        Long carWashId = 1L;
//        List<Long> serviceIds = List.of(1L, 2L);
//        String startTime = Instant.now().toString();
//        OrderRequest orderRequest = new OrderRequest(carWashId, serviceIds, startTime, PaymentType.SBP);
//        long mockDuration = 2000L;
//        Instant endTime = Instant.parse(startTime).plusMillis(mockDuration);
//        Long boxId = 1L;
//        Order expectedOrder = Order.builder()
//                .id(1L)
//                .username(username)
//                .carWashId(carWashId)
//                .serviceIds(serviceIds)
//                .startTime(Instant.parse(startTime))
//                .endTime(endTime)
//                .boxId(boxId)
//                .typePayment(PaymentType.SBP)
//                .status(OrderState.WAIT_CONFIRM)
//                .createdAt(Instant.now())
//                .build();
//        Mockito.when(remoteServicesService.getServicesDuration(serviceIds)).thenReturn(mockDuration);
//        Mockito.when(boxService.getBoxForOrder(carWashId, new Interval(Instant.parse(startTime), endTime))).thenReturn(Optional.of(boxId));
//        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(expectedOrder);
//        OrderDTO actualOrderDTO = orderService.saveOrder(username, orderRequest);
//        OrderDTO expectedOrderDTO = OrderDTO.builder()
//                .id(1L)
//                .username(username)
//                .carWashId(carWashId)
//                .boxId(boxId)
//                .status(OrderState.WAIT_CONFIRM.name())
//                .startTime(startTime)
//                .serviceIds(serviceIds)
//                .endTime(endTime.toString())
//                .typePayment(PaymentType.SBP.name())
//                .createdAt(actualOrderDTO.getCreatedAt())
//                .build();
//        Assertions.assertEquals(expectedOrderDTO, actualOrderDTO);
//        Mockito.verify(orderRepository).save(Mockito.any(Order.class));
//        Mockito.verify(eventService).sendOrderCreatedEvent(expectedOrderDTO);
//    }
//
//    /**
//     * Тест сохранения заказа при не имеющихся свободных боксах
//     */
//    @Test
//    void saveOrderWithNotFoundFreeBox() {
//        OrderRequest orderRequest = new OrderRequest(1L, List.of(1L, 2L), Instant.now().toString(), PaymentType.SBP);
//        Mockito.when(boxService.getBoxForOrder(Mockito.any(), Mockito.any())).thenReturn(Optional.empty());
//        BoxFreeNotFoundException exception = Assertions.assertThrows(
//                BoxFreeNotFoundException.class, () -> orderService.saveOrder("test", orderRequest)
//        );
//        Assertions.assertEquals("Free box not found", exception.getMessage());
//    }
//
//    /**
//     * Тест обработки назначения работника несуществующему заказу
//     */
//    @Test
//    void applyEmployeeOnNotExistsOrder() {
//        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//        OrderNotFoundException exception = Assertions.assertThrows(
//                OrderNotFoundException.class, () -> orderService.applyOrderEmployee(1L)
//        );
//        Assertions.assertEquals("Order with id 1 not found", exception.getMessage());
//        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест обработки назначения работника заказу
//     */
//    @Test
//    void applyOrderEmployee() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setState(OrderState.WAIT_CONFIRM);
//        Mockito.when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(order));
//        orderService.applyOrderEmployee(1L);
//        Assertions.assertEquals(OrderState.WAIT_PROCESS, order.getState());
//        Mockito.verify(orderRepository).save(order);
//    }
//
//    /**
//     * Тест удаления работника с несуществующего заказа
//     */
//    @Test
//    void disappointEmployeeFromNotExistsOrder() {
//        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());
//        OrderNotFoundException exception = Assertions.assertThrows(
//                OrderNotFoundException.class, () -> orderService.disappointEmployeeFromOrder(1L)
//        );
//        Assertions.assertEquals("Order with id 1 not found", exception.getMessage());
//        Mockito.verify(orderRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    /**
//     * Тест удаления работника с заказа
//     */
//    @Test
//    void disappointEmployee() {
//        Order order = new Order();
//        order.setId(1L);
//        order.setState(OrderState.WAIT_PROCESS);
//        Mockito.when(orderRepository.findById(Mockito.any())).thenReturn(Optional.of(order));
//        orderService.disappointEmployeeFromOrder(1L);
//        Assertions.assertEquals(OrderState.WAIT_CONFIRM, order.getState());
//        Mockito.verify(orderRepository).save(order);
//    }
//
//    /**
//     * Тест удаления заказа
//     */
//    @Test
//    void deleteOrder() {
//        orderService.deleteOrder(1L);
//        orderService.deleteOrder("test-user", 2L);
//        Mockito.verify(orderRepository).deleteById(1L);
//        Mockito.verify(orderRepository).deleteByUsernameAndId("test-user", 2L);
//    }
//
//}