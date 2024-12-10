package ru.anykeyers.orderservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.orderservice.service.UserOrderService;
import ru.anykeyers.orderservice.web.dto.OrderCreateRequest;
import ru.anykeyers.orderservice.web.mapper.OrderMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.USER_ORDER_CONTROLLER)
@Tag(name = "Обработка заказов пользователя")
public class UserOrderController {

    private final OrderMapper orderMapper;

    private final UserOrderService userOrderService;

    @Operation(summary = "Получить все активные заказы пользователя")
    @GetMapping("/active")
    public List<OrderDTO> getActiveOrders(@AuthenticationPrincipal Jwt jwt) {
        return orderMapper.toDTO(
                userOrderService.getActiveOrders(JwtUtils.extractUser(jwt))
        );
    }

    @Operation(summary = "Получить все завершенные заказы пользователя")
    @GetMapping("/processed")
    public List<OrderDTO> getProcessedOrders(@AuthenticationPrincipal Jwt jwt) {
        return orderMapper.toDTO(
                userOrderService.getProcessedOrders(JwtUtils.extractUser(jwt))
        );
    }

    @Operation(summary = "Сохранить заказ пользователя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody OrderCreateRequest createRequest) {
        Order order = userOrderService.createOrder(JwtUtils.extractUser(jwt), createRequest);
        return orderMapper.toDTO(order);
    }

}
