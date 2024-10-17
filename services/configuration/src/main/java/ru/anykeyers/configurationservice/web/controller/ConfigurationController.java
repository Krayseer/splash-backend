package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.utils.JwtUtils;
import ru.anykeyers.configurationservice.web.dto.ConfigurationInfoDTO;
import ru.anykeyers.configurationservice.web.mapper.ConfigurationMapper;
import ru.anykeyers.configurationservice.service.ConfigurationService;
import ru.anykeyers.configurationservice.web.ControllerName;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ControllerName.CONFIGURATION_CONTROLLER)
@Tag(name = "Обработка конфигураций автомоек")
public class ConfigurationController {

    private final ConfigurationMapper configurationMapper;

    private final ConfigurationService configurationService;

    @Operation(summary = "Получить конфигурацию автомойки авторизованного пользователя")
    @GetMapping
    public ConfigurationInfoDTO getUserConfiguration(@AuthenticationPrincipal Jwt jwt) {
        return configurationMapper.toFullDTO(configurationService.getConfiguration(JwtUtils.extractUser(jwt)));
    }

    @Operation(summary = "Получить конфигурацию автомойки по идентификатору")
    @GetMapping("/{id}")
    public ConfigurationInfoDTO getConfigurationById(@PathVariable Long id) {
        return configurationMapper.toFullDTO(configurationService.getConfiguration(id));
    }

    @Operation(summary = "Получить список всех автомоек")
    @GetMapping("/all")
    public List<ConfigurationInfoDTO> getAllConfigurations() {
        return configurationService.getAllConfigurations().stream()
                .map(configurationMapper::toFullDTO)
                .toList();
    }

    @Operation(summary = "Сохранить конфигурацию автомойки")
    @PostMapping
    public void saveConfiguration(@AuthenticationPrincipal Jwt jwt,
                                  @RequestBody ConfigurationDTO configurationDTO) {
        configurationService.registerConfiguration(JwtUtils.extractUser(jwt), configurationDTO);
    }

    @Operation(summary = "Обновить конфигурацию автомойки")
    @PutMapping
    public void updateConfiguration(@ModelAttribute ConfigurationDTO configurationDTO) {
        configurationService.updateConfiguration(configurationDTO);
    }

    @Operation(summary = "Удалить конфигурацию автомойки")
    @DeleteMapping
    public void deleteConfiguration(@AuthenticationPrincipal Jwt jwt) {
        configurationService.deleteConfiguration(JwtUtils.extractUser(jwt));
    }

}
