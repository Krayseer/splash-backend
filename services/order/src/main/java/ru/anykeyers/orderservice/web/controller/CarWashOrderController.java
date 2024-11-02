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
import ru.anykeyers.orderservice.service.CarWashService;
import ru.anykeyers.orderservice.web.mapper.OrderMapper;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.CAR_WASH_ORDER_CONTROLLER)
@Tag(name = "Обработка заказов автомойки")
public class CarWashOrderController {

    private final OrderMapper orderMapper;

    private final CarWashService orderService;

    @Operation(summary = "Получить список заказов автомойки")
    @GetMapping("/{carWashId}")
    public List<OrderDTO> getCarWashOrders(@PathVariable Long carWashId) {
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

    @Operation(summary = "Получить количество заказов, ожидающих одобрения")
    @GetMapping("/wait-confirm/count")
    public int getWaitConfirmOrdersCount(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId
    ) {
        return orderService.getOrdersCount(carWashId, OrderState.WAIT_CONFIRM);
    }

    @Operation(summary = "Получить количество активных заказов автомойки")
    @GetMapping("/active/count")
    public int getActiveOrdersCount(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId
    ) {
        return orderService.getOrdersCount(carWashId, OrderState.WAIT_PROCESS);
    }

    @Operation(summary = "Получить количество заказов, находящихся в обработке")
    @GetMapping("/processing/count")
    public int getProcessingOrdersCount(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId
    ) {
        return orderService.getOrdersCount(carWashId, OrderState.PROCESSING);
    }

    @Operation(summary = "Получить количество обработанных заказов")
    @GetMapping("/processed/count")
    public int getProcessedOrdersCount(
            @Parameter(description = "Идентификатор автомойки") @RequestParam("carWashId") Long carWashId
    ) {
        return orderService.getOrdersCount(carWashId, OrderState.PROCESSED);
    }

}

