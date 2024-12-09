package ru.anykeyers.orderservice.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.commonsapi.utils.DateUtils;
import ru.anykeyers.orderservice.domain.Order;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Тесты для {@link OrderCalculator}
 */
@ExtendWith(MockitoExtension.class)
class OrderCalculatorTest {

    @Mock
    private RemoteServicesService remoteServicesService;

    @InjectMocks
    private OrderCalculator orderCalculator;

    /**
     * Тест расчета окончания заказа в зависимости от заказов
     */
    @Test
    void calculateOrderEndTime() {
        // Подготовка
        long startTime = 500L;
        List<Long> serviceIds = List.of(2L, 3L);
        Mockito.when(remoteServicesService.getServicesDuration(serviceIds)).thenReturn(500L);

        // Действие
        long actualTime = orderCalculator.calculateOrderEndTime(startTime, serviceIds);

        // Проверка
        Assertions.assertEquals(1000L, actualTime);
    }

    /**
     * Тест получения свободных отрезков времени для заказов для автомойки, у которой не установлено время работы
     */
    @Test
    void calculateOrderFreeTimes_withNotSettingOpenAndCloseTimes() {
        // Подготовка
        Instant date = DateUtils.toInstant("16-03-2024");

        // Действие
        Set<Interval> actualTimePeriods = orderCalculator.calculateOrderFreeTimes(
                Collections.emptyList(), date.toEpochMilli(), date.plus(1, ChronoUnit.DAYS).toEpochMilli()
        );

        // Проверка
        List<Interval> expectedTimePeriods = List.of(
                Interval.of(Instant.parse("2024-03-16T00:00:00Z").toEpochMilli(), Instant.parse("2024-03-17T00:00:00Z").toEpochMilli())
        );
        Assertions.assertArrayEquals(expectedTimePeriods.toArray(), actualTimePeriods.toArray());
    }

    /**
     * Тест получения свободных отрезков времени для заказов у автомойки
     */
    @Test
    void calculateOrderFreeTimes() {
        // Подготовка
        Instant date = DateUtils.toInstant("16-03-2024");
        Order firstBoxOrder = Order.builder()
                .startTime(DateUtils.addTimeToInstant(date, "09:00").toEpochMilli())
                .endTime(DateUtils.addTimeToInstant(date, "10:00").toEpochMilli())
                .boxId(2L)
                .build();
        Order secondBoxOrder = Order.builder()
                .startTime(DateUtils.addTimeToInstant(date, "13:00").toEpochMilli())
                .endTime(DateUtils.addTimeToInstant(date, "13:30").toEpochMilli())
                .boxId(3L)
                .build();
        List<Order> orders = List.of(firstBoxOrder, secondBoxOrder);

        // Действие
        Set<Interval> actualIntervals = orderCalculator.calculateOrderFreeTimes(
                orders,
                DateUtils.addTimeToInstant(date, "08:00").toEpochMilli(),
                DateUtils.addTimeToInstant(date, "23:00").toEpochMilli()
        );

        Set<Interval> expectedTimePeriods = Set.of(
                Interval.of(Instant.parse("2024-03-16T08:00:00Z").toEpochMilli(), Instant.parse("2024-03-16T09:00:00Z").toEpochMilli()),
                Interval.of(Instant.parse("2024-03-16T10:00:00Z").toEpochMilli(), Instant.parse("2024-03-16T23:00:00Z").toEpochMilli()),
                Interval.of(Instant.parse("2024-03-16T08:00:00Z").toEpochMilli(), Instant.parse("2024-03-16T13:00:00Z").toEpochMilli()),
                Interval.of(Instant.parse("2024-03-16T13:30:00Z").toEpochMilli(), Instant.parse("2024-03-16T23:00:00Z").toEpochMilli())
        );
        Assertions.assertEquals(expectedTimePeriods, actualIntervals);
    }

    /**
     * Тест получения свободных отрезков времени в день, когда все время занято
     */
    @Test
    void calculateOrderFreeTimesWithNotExistsFreeTimeRanges() {
        // Подготовка
        Instant date = DateUtils.toInstant("16-03-2024");
        Order firstBoxOrder = Order.builder()
                .startTime(DateUtils.addTimeToInstant(date, "08:00").toEpochMilli())
                .endTime(DateUtils.addTimeToInstant(date, "15:00").toEpochMilli())
                .boxId(2L)
                .build();
        Order secondBoxOrder = Order.builder()
                .startTime(DateUtils.addTimeToInstant(date, "15:00").toEpochMilli())
                .endTime(DateUtils.addTimeToInstant(date, "23:00").toEpochMilli())
                .boxId(2L)
                .build();
        List<Order> orders = new ArrayList<>();
        orders.add(firstBoxOrder);
        orders.add(secondBoxOrder);

        // Действие
        Set<Interval> actualIntervals = orderCalculator.calculateOrderFreeTimes(
                orders,
                DateUtils.addTimeToInstant(date, "08:00").toEpochMilli(),
                DateUtils.addTimeToInstant(date, "23:00").toEpochMilli()
        );

        // Проверка
        Assertions.assertTrue(actualIntervals.isEmpty());
    }

}