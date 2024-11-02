package ru.anykeyers.commonsapi.domain.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.anykeyers.commonsapi.domain.Address;
import ru.anykeyers.commonsapi.domain.service.ServiceDTO;

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
public class ConfigurationDTO {

    private Long id;

    private UUID userId;

    private OrganizationInfo organizationInfo;

    private List<ServiceDTO> services;

    private List<BoxDTO> boxes;

    private String openTime;

    private String closeTime;

    private OrderProcessMode orderProcessMode;

    private List<String> photoUrls;

    private String videoId;

    private String createdAt;

}
