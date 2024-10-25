package ru.anykeyers.configurationservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.domain.configuration.OrderProcessMode;
import ru.anykeyers.commonsapi.domain.configuration.OrganizationInfo;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;
import ru.anykeyers.commonsapi.domain.configuration.BoxDTO;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Информационное DTO о конфигурации автомойки
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationInfoDTO {

    private Long id;

    private UUID userId;

    private OrganizationInfo organizationInfo;

    private Address address;

    private List<ServiceDTO> services;

    private List<BoxDTO> boxes;

    private List<String> photoUrls;

    private Instant openTime;

    private Instant closeTime;

    private OrderProcessMode orderProcessMode;

    private String videoId;

}
