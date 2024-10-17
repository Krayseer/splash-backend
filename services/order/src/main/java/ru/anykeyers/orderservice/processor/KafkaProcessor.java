//package ru.anykeyers.orderservice.processor;
//
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import ru.anykeyers.commonsapi.MessageQueue;
//import ru.anykeyers.commonsapi.domain.order.OrderDTO;
//import ru.anykeyers.orderservice.service.EmployeeService;
//import ru.anykeyers.orderservice.service.OrderService;
//
///**
// * Обработчик сообщений, поступающих по Kafka
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class KafkaProcessor {
//
//    private static final String GROUP_ID = "order-group";
//
//    private final EmployeeService employeeService;
//
//    private final OrderService orderService;
//
//    /**
//     * Слушатель события назначения работника заказу
//     */
//    @SneakyThrows
//    @KafkaListener(topics = MessageQueue.EMPLOYEE_ORDER_APPLY, groupId = GROUP_ID)
//    public void receiveOrderApplyEmployee(OrderDTO order) {
//        employeeService.applyOrderEmployee(order.getId());
//    }
//
//    /**
//     * Слушатель события удаления работника с заказа
//     */
//    @KafkaListener(topics = MessageQueue.ORDER_EMPLOYEE_DISAPPOINT, groupId = GROUP_ID)
//    public void receiveOrderEmployeeDisappointed(OrderDTO order) {
//        employeeService.disappointEmployeeFromOrder(order.getId());
//    }
//
//    /**
//     * Слушатель события об удалении заказа
//     */
//    @SneakyThrows
//    @KafkaListener(topics = MessageQueue.ORDER_DELETE, groupId = GROUP_ID)
//    public void receiveOrderDelete(OrderDTO order) {
//        orderService.deleteOrder(order.getId());
//    }
//
//}
