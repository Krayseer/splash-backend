package ru.anykeyers.orderservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.utils.DateUtils;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.order.OrderState;
import ru.anykeyers.commonsapi.domain.Interval;
import ru.anykeyers.orderservice.service.CarWashOrderService;
import ru.anykeyers.orderservice.web.mapper.OrderMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.CAR_WASH_ORDER_CONTROLLER)
@Tag(name = "Обработка заказов автомойки")
public class CarWashOrderController {

    private final OrderMapper orderMapper;

    private final CarWashOrderService orderService;

    @Operation(summary = "Получить список заказов автомойки")
    @GetMapping("/{carWashId}")
    public List<OrderDTO> getCarWashOrders(
            @Parameter(description = "Идентификатор автомойки") @PathVariable Long carWashId
    ) {
        return orderMapper.toDTO(
                orderService.getCarWashOrders(carWashId)
        );
    }

    @Operation(summary = "Получить список свободных отрезков времени в конкретный день")
    @GetMapping("/free-times")
    public Set<Interval> getOrderFreeTimes(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId,
            @Parameter(description = "Дата для получения свободных отрезков") @RequestParam("date") String date
    ) {
        return orderService.getOrderFreeTimes(carWashId, DateUtils.toInstant(date));
    }

    @Operation(summary = "Получить список заказов автомойки в конкретный день")
    @GetMapping("/by-date")
    public List<OrderDTO> getOrdersByDate(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId,
            @Parameter(description = "Дата для получения заказов") @RequestParam("date") String date
    ) {
        return orderMapper.toDTO(
                orderService.findOrdersByDate(carWashId, DateUtils.toInstant(date))
        );
    }

    @Operation(summary = "Получить список заказов, ожидающих одобрения")
    @GetMapping("/wait-confirm")
    public List<OrderDTO> getWaitConfirmOrders(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId
    ) {
        return orderMapper.toDTO(
                orderService.getWaitConfirmOrders(carWashId)
        );
    }

    @Operation(summary = "Получить карту соответствия заказов в зависимости от статуса заказа")
    @GetMapping("/count")
    public Map<OrderState, Long> getOrdersCount(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId
    ) {
        return orderService.getOrdersCountByState(carWashId);
    }

}

