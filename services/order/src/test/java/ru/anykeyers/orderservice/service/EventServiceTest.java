//package ru.anykeyers.orderservice.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.kafka.core.KafkaTemplate;
//import ru.anykeyers.commonsapi.domain.order.OrderState;
//import ru.anykeyers.commonsapi.domain.PaymentType;
//import ru.anykeyers.commonsapi.domain.order.OrderDTO;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
///**
// * Тесты для {@link EventService}
// */
//@ExtendWith(MockitoExtension.class)
//class EventServiceTest {
//
//    @Mock
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @InjectMocks
//    private EventService eventService;
//
//    @BeforeEach
//    void setUp() {
//        eventService = new EventService(new ObjectMapper(), kafkaTemplate);
//    }
//
//    /**
//     * Тест отправки события о создании заказа
//     */
//    @Test
//    void sendOrderCreatedEvent() {
//        Instant now = Instant.ofEpochMilli(1000000L);
//        OrderDTO orderDTO = OrderDTO.builder()
//                .id(1L)
//                .username("test-user")
//                .carWashId(1L)
//                .boxId(2L)
//                .status(OrderState.WAIT_CONFIRM.name())
//                .startTime(now.plus(1, ChronoUnit.DAYS).toString())
//                .endTime(now.plus(2, ChronoUnit.DAYS).toString())
//                .serviceIds(List.of(3L, 4L))
//                .typePayment(PaymentType.SBP.name())
//                .createdAt(now.toString())
//                .build();
//        eventService.sendOrderCreatedEvent(orderDTO);
//        Mockito.verify(kafkaTemplate, Mockito.times(1)).send("order-create",
//                "{" +
//                        "\"id\":1," +
//                        "\"username\":\"test-user\"," +
//                        "\"carWashId\":1," +
//                        "\"boxId\":2," +
//                        "\"status\":\"WAIT_CONFIRM\"," +
//                        "\"serviceIds\":[3,4]," +
//                        "\"startTime\":\"1970-01-02T00:16:40Z\"," +
//                        "\"endTime\":\"1970-01-03T00:16:40Z\"," +
//                        "\"typePayment\":\"SBP\"," +
//                        "\"createdAt\":\"1970-01-01T00:16:40Z\"}"
//        );
//    }
//
//}