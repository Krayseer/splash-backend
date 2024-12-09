package ru.anykeyers.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.commonsapi.utils.DateUtils;
import ru.anykeyers.orderservice.calculator.OrderCalculator;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.repository.OrderRepository;
import ru.anykeyers.orderservice.service.CarWashOrderService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Релизация сервиса обрабокти заказов автомойки
 */
@Service
@RequiredArgsConstructor
public class CarWashOrderServiceImpl implements CarWashOrderService {

    private final OrderRepository orderRepository;

    private final OrderCalculator orderCalculator;

    private final RemoteConfigurationService remoteConfigurationService;

    @Override
    public List<Order> getCarWashOrders(Long carWashId) {
        return orderRepository.findByCarWashId(carWashId);
    }

    @Override
    public List<Order> findOrdersByDate(Long carWashId, Instant date) {
        return orderRepository.findCarWashOrdersByStatesAndInterval(
                carWashId,
                List.of(OrderState.WAIT_CONFIRM, OrderState.WAIT_PROCESS, OrderState.PROCESSING),
                date.toEpochMilli(),
                date.plus(1, ChronoUnit.DAYS).toEpochMilli()
        );
    }

    @Override
    public List<Order> getWaitConfirmOrders(Long carWashId) {
        return orderRepository.findByCarWashIdAndState(carWashId, OrderState.WAIT_CONFIRM);
    }

    @Override
    public Map<OrderState, Long> getOrdersCountByState(Long carWashId) {
        return Arrays.stream(OrderState.values())
                .collect(Collectors.toMap(s -> s, s -> orderRepository.countByCarWashIdAndState(carWashId, s)));

    }

    @Override
    public Set<Interval> getOrderFreeTimes(Long carWashId, Instant date) {
        ConfigurationDTO configuration = remoteConfigurationService.getConfiguration(carWashId);
        Instant startTime = Optional.ofNullable(configuration.getOpenTime())
                .map(openTime -> DateUtils.addTimeToInstant(date, openTime))
                .orElse(date);
        Instant endTime = Optional.ofNullable(configuration.getCloseTime())
                .map(closeTime -> DateUtils.addTimeToInstant(date, closeTime))
                .orElse(date.plus(1, ChronoUnit.DAYS));
        return orderCalculator.calculateOrderFreeTimes(
                getCarWashOrders(carWashId), startTime.toEpochMilli(), endTime.toEpochMilli()
        );
    }

}
