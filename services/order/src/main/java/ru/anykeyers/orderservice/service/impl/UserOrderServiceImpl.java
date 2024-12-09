package ru.anykeyers.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anykeyers.commonsapi.MessageQueue;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.orderservice.calculator.BoxCalculator;
import ru.anykeyers.orderservice.calculator.OrderCalculator;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.repository.OrderRepository;
import ru.anykeyers.orderservice.service.CarWashOrderService;
import ru.anykeyers.orderservice.service.UserOrderService;
import ru.anykeyers.orderservice.web.dto.OrderCreateRequest;
import ru.anykeyers.orderservice.web.mapper.OrderMapper;

import java.util.List;

/**
 * Реализация сервиса обработки заказов пользователя
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {

    private final BoxCalculator boxCalculator;

    private final OrderCalculator orderCalculator;

    private final OrderRepository orderRepository;

    private final CarWashOrderService carWashService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final OrderMapper orderMapper;

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
    public Order createOrder(User user, OrderCreateRequest createRequest) {
        Order order = Order.builder()
                .userId(user.getId())
                .carWashId(createRequest.getCarWashId())
                .serviceIds(createRequest.getServiceIds())
                .typePayment(createRequest.getPaymentType())
                .state(OrderState.WAIT_CONFIRM)
                .startTime(createRequest.getStartTime())
                .endTime(orderCalculator.calculateOrderEndTime(createRequest.getStartTime(), createRequest.getServiceIds()))
                .build();
        order.setBoxId(
                boxCalculator.findFreeBox(
                        order.getCarWashId(),
                        carWashService.getCarWashOrders(order.getCarWashId()),
                        Interval.of(order.getStartTime(), order.getEndTime())
                )
        );
        Order savedOrder = orderRepository.save(order);
        kafkaTemplate.send(MessageQueue.ORDER_CREATE, orderMapper.toDTO(savedOrder));
        log.info("Save new order: {}", savedOrder);
        return savedOrder;
    }

}
