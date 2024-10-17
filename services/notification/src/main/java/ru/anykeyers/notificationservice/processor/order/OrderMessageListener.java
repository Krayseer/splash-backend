//package ru.anykeyers.notificationservice.processor.order;
//
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import ru.anykeyers.commonsapi.domain.order.OrderDTO;
//import ru.anykeyers.commonsapi.MessageQueue;
//
///**
// * Слушатель сообщений с Kafka
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OrderMessageListener {
//
//    private static final String GROUP_ID = "notification-group";
//
//    private final OrderProcessor orderProcessor;
//
//    /**
//     * Слушатель создания заказов
//     */
//    @KafkaListener(topics = MessageQueue.ORDER_CREATE, groupId = GROUP_ID)
//    public void receiveOrderCreate(OrderDTO order) {
//        orderProcessor.processOrderCreate(order);
//    }
//
//    /**
//     * Слушатель события об удалении заказа
//     */
//    @SneakyThrows
//    @KafkaListener(topics = MessageQueue.ORDER_DELETE, groupId = GROUP_ID)
//    public void receiveOrderDelete(OrderDTO order) {
//        orderProcessor.processOrderDelete(order);
//    }
//
//}
