package ru.anykeyers.orderservice.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationInfoDTO;
import ru.anykeyers.commonsapi.domain.order.OrderDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.commonsapi.remote.RemoteConfigurationService;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.commonsapi.remote.RemoteUserService;
import ru.anykeyers.orderservice.domain.Order;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ModelMapper modelMapper;

    private final RemoteConfigurationService remoteConfigurationService;

    private final RemoteUserService remoteUserService;

    private final RemoteServicesService remoteServicesService;

    public OrderDTO toDTO(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        ConfigurationInfoDTO configuration = remoteConfigurationService.getConfiguration(order.getCarWashId());
        User user = remoteUserService.getUser(order.getUserId());
        dto.setBox(configuration.getBoxes().stream().filter(x -> x.getId().equals(order.getBoxId())).findFirst().orElse(null));
        dto.setUser(user);
        dto.setServices(remoteServicesService.getServices(order.getServiceIds()));
        return dto;
    }

}
