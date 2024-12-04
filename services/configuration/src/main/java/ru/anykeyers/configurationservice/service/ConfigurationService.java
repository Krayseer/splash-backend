package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.web.dto.ConfigurationRegisterRequest;
import ru.anykeyers.configurationservice.web.dto.ConfigurationUpdateRequest;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Сервис обработки конфигурации автомойки
 */
public interface ConfigurationService {

    /**
     * Получить информацию обо всех автомойках
     */
    List<Configuration> getAllConfigurations();

    /**
     * Получить конфигурацию автомойки
     *
     * @param user пользователь - хозяин автомойки
     */
    Configuration getConfiguration(User user);

    /**
     * Получить конфигурацию автомойки в PDF формате
     *
     * @param userId идентификатор пользователя - хозяина автомойки
     */
    ByteArrayOutputStream getConfigurationPdf(UUID userId);

    /**
     * Получить конфигурацию автомойки
     *
     * @param userId идентификатор пользователя
     */
    Configuration getConfiguration(UUID userId);

    /**
     * Получить информацию об автомойке
     *
     * @param id идентификатор автомойки
     */
    Configuration getConfiguration(Long id);

    /**
     * Зарегистрировать автомойку
     *
     * @param user             пользователь, регистрирующий автомойку
     * @param registerRequest  данные для регистрации автомойки
     */
    void registerConfiguration(User user, ConfigurationRegisterRequest registerRequest);

    /**
     * Обновить данные об автомойке
     *
     * @param updateRequest обновлённая конфигурация автомойки
     */
    void updateConfiguration(User user, ConfigurationUpdateRequest updateRequest);

    /**
     * Удалить конфигурацию автомойки
     *
     * @param user хозяин автомойки
     */
    void deleteConfiguration(User user);
}
