package ru.anykeyers.orderservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.orderservice.service.OrderService;
import ru.anykeyers.orderservice.web.mapper.OrderMapper;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BASE_CONTROLLER)
@Tag(name = "Обработка заказов")
public class OrderController {

    private final OrderMapper orderMapper;

    private final OrderService orderService;

    @Operation(summary = "Получить заказ по идентификатору")
    @GetMapping("/{id}")
    public OrderDTO getOrderById(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long id
    ) {
        return orderMapper.toDTO(orderService.getOrder(id));
    }

    @Operation(summary = "Получить список заказов по идентификаторам")
    @GetMapping("/list")
    public List<OrderDTO> getOrders(
            @Parameter(description = "Идентификаторы заказов") @RequestParam("order-ids") Long[] orderIds
    ) {
        return orderMapper.toDTO(
                orderService.getOrders(Arrays.stream(orderIds).toList())
        );
    }

    @Operation(summary = "Удалить заказ пользователя")
    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @Parameter(description = "Идентификатор заказа") @PathVariable Long orderId
    ) {
        orderService.deleteOrder(orderId);
    }

}
