package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.web.dto.ConfigurationRegisterRequest;
import ru.anykeyers.configurationservice.web.dto.ConfigurationUpdateRequest;

import java.util.List;

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
