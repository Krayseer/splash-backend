package ru.anykeyers.businessorderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.businessorderservice.domain.BusinessOrderRequest;
import ru.anykeyers.businessorderservice.service.OrderService;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BASE_CONTROLLER)
@Tag(name = "Обработка бизнес заказов")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Получить список заказов текущего работника")
    @GetMapping("/employee")
    public List<OrderDTO> getCurrentEmployeeOrders(@AuthenticationPrincipal Jwt jwt) {
        return orderService.getOrders(jwt.getSubject());
    }

    @Operation(summary = "Получить список заказов работника")
    @GetMapping("/{username}")
    public List<OrderDTO> getEmployeeOrders(@PathVariable String username) {
        return orderService.getOrders(username);
    }

    @Operation(summary = "Получить свободных работников на заказ")
    @GetMapping("/free-employees/{orderId}")
    public List<User> getFreeEmployeesOrder(@PathVariable Long orderId) {
        return orderService.getFreeEmployees(orderId);
    }

    @Operation(summary = "Назначить работника на заказ")
    @PostMapping("/appoint")
    public void appointOrderEmployee(@RequestBody BusinessOrderRequest request) {
        orderService.appointOrderEmployee(request.getOrderId(), request.getEmployeeId());
    }

    @Operation(summary = "Убрать работника с заказа")
    @PostMapping("/disappoint/{businessOrderId}")
    public void disappointOrderEmployee(
            @Parameter(description = "Идентификатор заказа") @PathVariable Long businessOrderId
    ) {
        orderService.disappointEmployeeFromOrder(businessOrderId);
    }

}
