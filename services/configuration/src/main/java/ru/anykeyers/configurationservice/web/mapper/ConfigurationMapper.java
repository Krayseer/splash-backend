package ru.anykeyers.configurationservice.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.anykeyers.commonsapi.remote.RemoteServicesService;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;

/**
 * Фабрика по созданию конфигруации
 */
@Component
@RequiredArgsConstructor
public class ConfigurationMapper {

    private final ModelMapper modelMapper;

    private final RemoteServicesService remoteServicesService;

    public ConfigurationDTO toDTO(Configuration configuration) {
        ConfigurationDTO configurationInfoDTO = modelMapper.map(configuration, ConfigurationDTO.class);
        configurationInfoDTO.setServices(remoteServicesService.getServices(configuration.getId()));
        return configurationInfoDTO;
    }

}
