package ru.anykeyers.configurationservice.service;

import ru.anykeyers.configurationservice.domain.Configuration;

/**
 * Сервис валидации
 */
public interface ValidationService {

    /**
     * Проверить валидность конфигурации автомойки
     *
     * @param configuration конфигурация автомойки
     */
    boolean validateTinConfiguration(Configuration configuration);

}
