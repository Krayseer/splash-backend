package ru.anykeyers.orderservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.orderservice.domain.Order;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.orderservice.service.UserOrderService;
import ru.anykeyers.orderservice.web.ControllerName;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.USER_ORDER_CONTROLLER)
@Tag(name = "Обработка заказов пользователя")
public class UserOrderController {

    private final ModelMapper modelMapper;

    private final UserOrderService userOrderService;

    @Operation(summary = "Получить все активные заказы пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение списка заказов пользователя",
                    content = {
                            @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
                    })
    })
    @GetMapping("/active")
    public List<OrderDTO> getActiveOrders(@AuthenticationPrincipal Jwt jwt) {
        return userOrderService.getActiveOrders(JwtUtils.extractUser(jwt)).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @Operation(summary = "Получить все завершенные заказы пользователя")
    @GetMapping("/processed")
    public List<OrderDTO> getProcessedOrders(@AuthenticationPrincipal Jwt jwt) {
        return userOrderService.getProcessedOrders(jwt.getSubject()).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @Operation(summary = "Сохранить заказ пользователя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@AuthenticationPrincipal Jwt jwt, @RequestBody OrderDTO orderDTO) {
        Order order = userOrderService.createOrder(jwt.getSubject(), orderDTO);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Operation(summary = "Удалить заказ пользователя")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable Long orderId) {
        userOrderService.deleteOrder(orderId);
    }

}
