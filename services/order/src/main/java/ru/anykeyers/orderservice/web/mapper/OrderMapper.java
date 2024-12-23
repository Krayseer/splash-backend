package ru.anykeyers.orderservice.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.orderservice.domain.Order;

import java.util.List;

/**
 * Маппер заказов
 */
@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ModelMapper modelMapper;

    private final RemoteServicesService remoteServicesService;

    private final RemoteUserService remoteUserService;

    private final RemoteConfigurationService remoteConfigurationService;

    /**
     * Преобразовать заказ в DTO
     *
     * @param order заказ
     */
    public OrderDTO toDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        ConfigurationDTO configuration = remoteConfigurationService.getConfiguration(order.getCarWashId());
        dto.setBox(configuration.getBoxes().stream().filter(x -> x.getId().equals(order.getBoxId())).findFirst().orElse(null));
        dto.setServices(remoteServicesService.getServices(order.getServiceIds()));
        dto.setUser(remoteUserService.getUser(order.getUserId()));
        return dto;
    }

    /**
     * Преобразовать список заказов в список DTO
     *
     * @param orders список заказов
     */
    public List<OrderDTO> toDTO(List<Order> orders) {
        return orders.stream().map(this::toDTO).toList();
    }

}
