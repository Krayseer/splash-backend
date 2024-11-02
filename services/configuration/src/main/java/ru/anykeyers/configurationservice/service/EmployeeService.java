package ru.anykeyers.configurationservice.service;

import ru.anykeyers.commonsapi.domain.user.User;
import ru.anykeyers.configurationservice.domain.Configuration;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис обработки работников автомойки
 */
public interface EmployeeService {

    /**
     * Получить список работников автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    Set<User> getCarWashEmployees(Long carWashId);

    /**
     * Добавить работника автомойке
     *
     * @param configuration автомойка
     * @param userId        идентификатор пользователя
     */
    void addCarWashEmployee(Configuration configuration, UUID userId);

    /**
     * Удалить работника с автомойки
     *
     * @param carWashId идентификатор автомойки
     * @param userId    идентификатор работника
     */
    void deleteEmployee(Long carWashId, UUID userId);

}
