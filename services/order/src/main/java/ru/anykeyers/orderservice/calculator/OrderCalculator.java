package ru.anykeyers.orderservice.calculator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.orderservice.domain.Order;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCalculator {

    private final RemoteServicesService remoteServicesService;

    /**
     * Рассчитать время окончания заказа
     */
    public long calculateOrderEndTime(long startTime, List<Long> serviceIds) {
        long duration = remoteServicesService.getServicesDuration(serviceIds);
        return Instant.ofEpochMilli(startTime).plusMillis(duration).toEpochMilli();
    }

    /**
     * Получить список свободных интервалов для заказов
     *
     * @param carWashOrders актуальный список заказов
     * @param startTime     начало
     * @param endTime       конец
     */
    public Set<Interval> calculateOrderFreeTimes(List<Order> carWashOrders, long startTime, long endTime) {
        if (carWashOrders.isEmpty()) {
            return Collections.singleton(Interval.of(startTime, endTime));
        }
        Map<Long, List<Order>> filteredOrdersByBoxId = carWashOrders.stream()
                .filter(o -> o.getStartTime() >= startTime)
                .filter(o -> o.getStartTime() <= endTime)
                .collect(Collectors.groupingBy(Order::getBoxId));
        return filteredOrdersByBoxId
                .values().stream()
                .flatMap(orders -> findFreeIntervals(getOrdersAsIntervals(orders), startTime, endTime).stream())
                .collect(Collectors.toSet());
    }

    private List<Interval> getOrdersAsIntervals(List<Order> orders) {
        return new ArrayList<>(orders.stream()
                .map(order -> new Interval(order.getStartTime(), order.getEndTime()))
                .toList());
    }

    private List<Interval> findFreeIntervals(List<Interval> busyIntervals, long startTime, long endTime) {
        if (busyIntervals.isEmpty()) {
            return Collections.singletonList(new Interval(startTime, endTime));
        }
        busyIntervals.sort(Comparator.comparing(Interval::getStart));
        List<Interval> freeIntervals = new ArrayList<>();
        freeIntervals.addAll(handleStartTime(busyIntervals, startTime));
        freeIntervals.addAll(handleBusyIntervals(busyIntervals, endTime));
        freeIntervals.addAll(handleEndTime(busyIntervals, endTime));
        return freeIntervals;
    }

    private Collection<? extends Interval> handleStartTime(List<Interval> busyIntervals, long startTime) {
        List<Interval> result = new ArrayList<>();
        if (startTime < busyIntervals.getFirst().getStart()) {
            result.add(new Interval(startTime, busyIntervals.getFirst().getStart()));
        }
        return result;
    }

    private Collection<? extends Interval> handleBusyIntervals(List<Interval> busyIntervals, long endTime) {
        List<Interval> result = new ArrayList<>();
        for (int i = 0; i < busyIntervals.size() - 1; i++) {
            Interval current = busyIntervals.get(i);
            Interval next = busyIntervals.get(i + 1);
            if (current.getEnd() < next.getStart() && next.getStart() < endTime) {
                result.add(new Interval(current.getEnd(), next.getStart()));
            }
        }
        return result;
    }

    private Collection<? extends Interval> handleEndTime(List<Interval> busyIntervals, long endTime) {
        List<Interval> result = new ArrayList<>();
        if (endTime > busyIntervals.getLast().getEnd()) {
            result.add(new Interval(busyIntervals.getLast().getEnd(), endTime));
        }
        return result;
    }

}
