package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.configurationservice.web.dto.ConfigurationRegisterRequest;
import ru.anykeyers.configurationservice.web.dto.ConfigurationUpdateRequest;
import ru.anykeyers.configurationservice.web.mapper.ConfigurationMapper;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.web.ControllerName;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.CONFIGURATION_CONTROLLER)
@Tag(name = "Обработка конфигураций автомоек")
public class ConfigurationController {

    private final ConfigurationMapper configurationMapper;

    private final ConfigurationService configurationService;

    @Operation(summary = "Получить конфигурацию автомойки авторизованного пользователя")
    @GetMapping
    public ConfigurationDTO getCurrentUserConfiguration(@AuthenticationPrincipal Jwt jwt) {
        return configurationMapper.toDTO(configurationService.getConfiguration(JwtUtils.extractUser(jwt)));
    }

    @Operation(summary = "Получить конфигурацию автомойки пользователя")
    @GetMapping("/user/{userId}")
    public ConfigurationDTO getUserConfiguration(@PathVariable UUID userId) {
        return configurationMapper.toDTO(configurationService.getConfiguration(userId));
    }

    @Operation(summary = "Получить конфигурацию автомойки по идентификатору")
    @GetMapping("/{id}")
    public ConfigurationDTO getConfigurationById(@PathVariable Long id) {
        return configurationMapper.toDTO(configurationService.getConfiguration(id));
    }

    @Operation(summary = "Получить список всех автомоек")
    @GetMapping("/all")
    public List<ConfigurationDTO> getAllConfigurations() {
        return configurationService.getAllConfigurations().stream()
                .map(configurationMapper::toDTO)
                .toList();
    }

    @Operation(summary = "Сохранить конфигурацию автомойки")
    @PostMapping
    public void saveConfiguration(@AuthenticationPrincipal Jwt jwt,
                                  @RequestBody ConfigurationRegisterRequest registerRequest) {
        configurationService.registerConfiguration(JwtUtils.extractUser(jwt), registerRequest);
    }

    @Operation(summary = "Обновить конфигурацию автомойки")
    @PutMapping
    public void updateConfiguration(@AuthenticationPrincipal Jwt jwt,
                                    @ModelAttribute ConfigurationUpdateRequest updateRequest) {
        configurationService.updateConfiguration(JwtUtils.extractUser(jwt), updateRequest);
    }

    @Operation(summary = "Удалить конфигурацию автомойки")
    @DeleteMapping
    public void deleteConfiguration(@AuthenticationPrincipal Jwt jwt) {
        configurationService.deleteConfiguration(JwtUtils.extractUser(jwt));
    }

}
