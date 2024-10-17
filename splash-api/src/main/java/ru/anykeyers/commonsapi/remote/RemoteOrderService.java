package ru.anykeyers.commonsapi.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;

import java.util.List;

/**
 * Удаленный сервис обработки заказов
 */
@FeignClient(
        name = "order-service",
        path = "/api/order"
)
public interface RemoteOrderService {

    @GetMapping("/{id}")
    OrderDTO getOrder(@PathVariable("id") long id);

    @GetMapping("/list")
    List<OrderDTO> getOrders(@RequestParam("order-ids") List<Long> orderIds);

    @GetMapping("/car-wash/by-date")
    List<OrderDTO> getOrders(@RequestParam("carWashId") Long carWashId, @RequestParam("date") String date);

}
