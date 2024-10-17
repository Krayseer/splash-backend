package ru.anykeyers.orderservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.orderservice.service.OrderService;
import ru.anykeyers.orderservice.web.ControllerName;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.BASE_CONTROLLER)
@Tag(name = "Обработка заказов")
public class OrderController {

    private final ModelMapper modelMapper;

    private final OrderService orderService;

    private final RemoteUserService remoteUserService;

    @Operation(summary = "Получить заказ по идентификатору")
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable Long id) {
        return modelMapper.map(orderService.getOrder(id), OrderDTO.class);
    }

    @Operation(summary = "Получить список заказов по идентификаторам")
    @GetMapping("/list")
    public List<OrderDTO> getOrders(@RequestParam("order-ids") Long[] orderIds) {
        return orderService.getOrders(Arrays.stream(orderIds).toList()).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .toList();
    }

    @GetMapping("/test")
    public User test() {
        return remoteUserService.getUser("test");
    }

}
