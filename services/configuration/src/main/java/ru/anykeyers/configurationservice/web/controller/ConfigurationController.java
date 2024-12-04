package ru.anykeyers.configurationservice.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import java.io.ByteArrayOutputStream;
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

    @Operation(summary = "Получить информацию об автомойке в PDF формате")
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getCurrentUserConfigurationPdf(@AuthenticationPrincipal Jwt jwt) {
        ByteArrayOutputStream document = configurationService.getConfigurationPdf(JwtUtils.extractUser(jwt).getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_carwash.pdf");
        headers.setContentLength(document.size());
        return new ResponseEntity<>(document.toByteArray(), headers, HttpStatus.OK);
    }

    @Operation(summary = "Получить информацию об автомойке в PDF формате")
    @GetMapping("/pdf/{userId}")
    public ResponseEntity<byte[]> getCurrentUserConfigurationPdf(@PathVariable UUID userId) {
        ByteArrayOutputStream document = configurationService.getConfigurationPdf(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=user_carwash.pdf");
        headers.setContentLength(document.size());
        return new ResponseEntity<>(document.toByteArray(), headers, HttpStatus.OK);
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
