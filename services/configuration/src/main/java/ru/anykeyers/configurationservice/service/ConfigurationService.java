package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.configuration.ConfigurationDTO;
import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;

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
     * @param user              пользователь, регистрирующий автомойку
     * @param configurationDTO  данные для регистрации автомойки
     */
    void registerConfiguration(User user, ConfigurationDTO configurationDTO);

    /**
     * Обновить данные об автомойке
     *
     * @param configurationDTO обновлённая конфигурация автомойки
     */
    void updateConfiguration(ConfigurationDTO configurationDTO);

    /**
     * Удалить конфигурацию автомойки
     *
     * @param user хозяин автомойки
     */
    void deleteConfiguration(User user);

}
