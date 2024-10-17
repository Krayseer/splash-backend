package ru.anykeyers.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.orderservice.service.EmployeeService;
import ru.anykeyers.orderservice.service.OrderService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final OrderService orderService;

    @Override
    public void applyOrderEmployee(Long orderId) {
        Order order = orderService.getOrder(orderId);
        order.setState(OrderState.WAIT_PROCESS);
        orderService.saveOrUpdate(order);
        log.info("Process order apply employee: {}", order);
    }

    @Override
    public void disappointEmployeeFromOrder(long orderId) {
        Order order = orderService.getOrder(orderId);
        order.setState(OrderState.WAIT_CONFIRM);
        orderService.saveOrUpdate(order);
        log.info("Disappoint employee from order: {}", order);
    }

}
