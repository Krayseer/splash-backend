package ru.anykeyers.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.repository.OrderRepository;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.domain.exception.OrderNotFoundException;
import ru.anykeyers.orderservice.service.OrderService;

import java.util.List;

/**
 * Реализация сервиса заказов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public List<Order> getOrders(List<Long> orderIds) {
        return orderRepository.findAllById(orderIds);
    }

    @Override
    public List<Order> getOrdersByState(OrderState orderState) {
        return orderRepository.findByState(orderState);
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
