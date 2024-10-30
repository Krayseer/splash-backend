package ru.anykeyers.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationInfoDTO;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.commonsapi.utils.DateUtils;
import ru.anykeyers.orderservice.repository.OrderRepository;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.domain.exception.OrderNotFoundException;
import ru.anykeyers.orderservice.calculator.OrderCalculator;
import ru.anykeyers.orderservice.service.OrderService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Реализация сервисов обработки заказов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;

    private final RemoteConfigurationService remoteConfigurationService;

    private final OrderRepository orderRepository;

    private final OrderCalculator orderCalculator;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public List<Order> getOrders(List<Long> orderIds) {
        return orderRepository.findAllById(orderIds);
    }

    @Override
    public List<Order> getOrders(OrderState orderState) {
        return orderRepository.findByState(orderState);
    }

    @Override
    public List<Order> getCarWashOrders(User user) {
        return orderRepository.findByUserId(user.getId());
    }

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
    public int getOrdersCount(Long carWashId, OrderState orderState) {
        return orderRepository.countByCarWashIdAndState(carWashId, orderState);
    }

    @Override
    public Set<Interval> getOrderFreeTimes(Long carWashId, Instant date) {
        ConfigurationInfoDTO configuration = remoteConfigurationService.getConfiguration(carWashId);
        Instant startTime = Optional.ofNullable(configuration.getOpenTime())
                .map(openTime -> DateUtils.addTimeToInstant(date, openTime))
                .orElse(date);
        Instant endTime = Optional.ofNullable(configuration.getCloseTime())
                .map(closeTime -> DateUtils.addTimeToInstant(date, closeTime))
                .orElse(date.plus(1, ChronoUnit.DAYS));
        return orderCalculator.getOrderFreeTimes(
                getCarWashOrders(carWashId), startTime.toEpochMilli(), endTime.toEpochMilli()
        );
    }

    @Override
    public List<Order> getActiveOrders(User user) {
        return orderRepository.findByUserIdAndStateIn(
                user.getId(), List.of(OrderState.WAIT_CONFIRM, OrderState.WAIT_PROCESS, OrderState.PROCESSING)
        );
    }

    @Override
    public List<Order> getProcessedOrders(User user) {
        return orderRepository.findByUserIdAndState(user.getId(), OrderState.PROCESSED);
    }

    @Override
    @Transactional
    public Order createOrder(User user, OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setUserId(user.getId());
        order.setEndTime(orderCalculator.calculateOrderEndTime(orderDTO));
        order.setBoxId(
                orderCalculator.findFreeBox(
                        order.getCarWashId(), getCarWashOrders(order.getCarWashId()), order.getStartTime(), order.getEndTime()
                )
        );
        Order savedOrder = orderRepository.save(order);
//        kafkaTemplate.send(MessageQueue.ORDER_CREATE, modelMapper.map(savedOrder, OrderDTO.class));
        log.info("Save new order: {}", orderDTO);
        return savedOrder;
    }

    @Override
    public void saveOrUpdate(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        log.info("Delete order: {}", orderId);
    }

}
