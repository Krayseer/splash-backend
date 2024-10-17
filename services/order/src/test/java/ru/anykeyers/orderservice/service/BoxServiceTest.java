//package ru.anykeyers.orderservice.service;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;
//import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
//import ru.anykeyers.commonsapi.service.RemoteConfigurationService;
//import ru.anykeyers.commonsapi.utils.DateUtils;
//import ru.anykeyers.orderservice.repository.OrderRepository;
//import ru.anykeyers.orderservice.domain.Order;
//import ru.anykeyers.commonsapi.domain.TimePeriod;
//import ru.anykeyers.orderservice.service.impl.BoxServiceImpl;
//
//import java.time.Instant;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
///**
// * Тесты для {@link BoxService}
// */
//@ExtendWith(MockitoExtension.class)
//class BoxServiceTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private RemoteConfigurationService remoteConfigurationService;
//
//    @InjectMocks
//    private BoxServiceImpl boxService;
//
//    /**
//     * Проверка получения идентификатора бокса для заказа с одним свободным боксом на установленное время
//     */
//    @Test
//    void getBoxForOrder() {
//        Long carWashId = 1L;
//        List<Long> boxIds = List.of(2L, 3L);
//        TimePeriod timePeriod = new TimePeriod(Instant.ofEpochMilli(600), Instant.ofEpochMilli(800));
//        Order firstBoxOrder = Order.builder()
//                .startTime(Instant.ofEpochMilli(700))
//                .endTime(Instant.ofEpochMilli(900))
//                .boxId(2L)
//                .build();
//        Order secondBoxOrder = Order.builder()
//                .startTime(Instant.ofEpochMilli(200))
//                .endTime(Instant.ofEpochMilli(1200))
//                .boxId(3L)
//                .build();
//        Mockito.when(remoteConfigurationService.getBoxIds(carWashId)).thenReturn(boxIds);
//        Mockito.when(orderRepository.findByBoxIdIn(boxIds)).thenReturn(List.of(firstBoxOrder, secondBoxOrder));
//        Assertions.assertEquals(Optional.of(3L), boxService.getBoxForOrder(carWashId, timePeriod));
//    }
//
//    /**
//     * Тест неудачного получения идентификатора бокса для заказа (когда на установленный отрезок времени уже есть
//     * другие невыполненные в данный момент заказы)
//     */
//    @Test
//    void getBoxForOrderWithNotExistsFreeRanges() {
//        Long carWashId = 1L;
//        List<Long> boxIds = List.of(2L, 3L);
//        TimePeriod timePeriod = new TimePeriod(Instant.ofEpochMilli(1000), Instant.ofEpochMilli(2000));
//        Order firstBoxOrder = Order.builder()
//                .startTime(Instant.ofEpochMilli(1200))
//                .endTime(Instant.ofEpochMilli(1300))
//                .boxId(2L)
//                .build();
//        Order secondBoxOrder = Order.builder()
//                .startTime(Instant.ofEpochMilli(1500))
//                .endTime(Instant.ofEpochMilli(1600))
//                .boxId(3L)
//                .build();
//        Mockito.when(remoteConfigurationService.getBoxIds(carWashId)).thenReturn(boxIds);
//        Mockito.when(orderRepository.findByBoxIdIn(boxIds)).thenReturn(List.of(firstBoxOrder, secondBoxOrder));
//        Optional<Long> actualBoxId = boxService.getBoxForOrder(carWashId, timePeriod);
//        Assertions.assertTrue(actualBoxId.isEmpty());
//    }
//
//    /**
//     * Тест получения свободных отрезков времени для заказов для автомойки, у которой не установлено время работы
//     */
//    @Test
//    void getOrderFreeTimesWithNotSettingOpenAndCloseTimes() {
//        Long carWashId = 1L;
//        ConfigurationDTO configuration = ConfigurationDTO.builder()
//                .id(carWashId)
//                .boxes(List.of(
//                        new BoxDTO(2L, "Box 1", carWashId),
//                        new BoxDTO(3L, "Box 2", carWashId)
//                ))
//                .build();
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Instant date = DateUtils.toInstant("16-03-2024");
//        Mockito.when(orderRepository.findByBoxIdIn(List.of(2L, 3L))).thenReturn(Collections.emptyList());
//        Set<TimePeriod> actualTimePeriods = boxService.getOrderFreeTimes(carWashId, date);
//        List<TimePeriod> expectedTimePeriods = List.of(
//                new TimePeriod(Instant.parse("2024-03-16T00:00:00Z"), Instant.parse("2024-03-17T00:00:00Z"))
//        );
//        Assertions.assertArrayEquals(expectedTimePeriods.toArray(), actualTimePeriods.toArray());
//    }
//
//    /**
//     * Тест получения свободных отрезков времени для заказов у автомойки
//     */
//    @Test
//    void getOrderFreeTimes() {
//        Long carWashId = 1L;
//        ConfigurationDTO configuration = ConfigurationDTO.builder()
//                .id(carWashId)
//                .openTime("08:00")
//                .closeTime("23:00")
//                .boxes(List.of(
//                        new BoxDTO(2L, "Box 1", carWashId),
//                        new BoxDTO(3L, "Box 2", carWashId)
//                ))
//                .build();
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Instant date = DateUtils.toInstant("16-03-2024");
//        Order firstBoxOrder = Order.builder()
//                .startTime(DateUtils.addTimeToInstant(date, "09:00"))
//                .endTime(DateUtils.addTimeToInstant(date, "10:00"))
//                .boxId(2L)
//                .build();
//        Order secondBoxOrder = Order.builder()
//                .startTime(DateUtils.addTimeToInstant(date, "13:00"))
//                .endTime(DateUtils.addTimeToInstant(date, "13:30"))
//                .boxId(3L)
//                .build();
//        Mockito.when(orderRepository.findByBoxIdIn(List.of(2L, 3L))).thenReturn(List.of(firstBoxOrder, secondBoxOrder));
//        Set<TimePeriod> actualTimePeriods = boxService.getOrderFreeTimes(carWashId, date);
//        Set<TimePeriod> expectedTimePeriods = Set.of(
//                new TimePeriod(Instant.parse("2024-03-16T08:00:00Z"), Instant.parse("2024-03-16T09:00:00Z")),
//                new TimePeriod(Instant.parse("2024-03-16T10:00:00Z"), Instant.parse("2024-03-16T23:00:00Z")),
//                new TimePeriod(Instant.parse("2024-03-16T08:00:00Z"), Instant.parse("2024-03-16T13:00:00Z")),
//                new TimePeriod(Instant.parse("2024-03-16T13:30:00Z"), Instant.parse("2024-03-16T23:00:00Z"))
//        );
//        Assertions.assertEquals(expectedTimePeriods, actualTimePeriods);
//    }
//
//    /**
//     * Тест получения свободных отрезков времени в день, когда все время занято
//     */
//    @Test
//    void getOrderFreeTimesWithNotExistsFreeTimeRanges() {
//        Long carWashId = 1L;
//        ConfigurationDTO configuration = ConfigurationDTO.builder()
//                .id(carWashId)
//                .openTime("08:00")
//                .closeTime("23:00")
//                .boxes(List.of(new BoxDTO(2L, "Box 1", carWashId)))
//                .build();
//        Mockito.when(remoteConfigurationService.getConfiguration(carWashId)).thenReturn(configuration);
//        Instant date = DateUtils.toInstant("16-03-2024");
//        Order firstBoxOrder = Order.builder()
//                .startTime(DateUtils.addTimeToInstant(date, "08:00"))
//                .endTime(DateUtils.addTimeToInstant(date, "15:00"))
//                .boxId(2L)
//                .build();
//        Order secondBoxOrder = Order.builder()
//                .startTime(DateUtils.addTimeToInstant(date, "15:00"))
//                .endTime(DateUtils.addTimeToInstant(date, "23:00"))
//                .boxId(2L)
//                .build();
//        Mockito.when(orderRepository.findByBoxIdIn(List.of(2L))).thenReturn(List.of(firstBoxOrder, secondBoxOrder));
//        Set<TimePeriod> actualTimePeriods = boxService.getOrderFreeTimes(carWashId, date);
//        Assertions.assertTrue(actualTimePeriods.isEmpty());
//    }
//
//}