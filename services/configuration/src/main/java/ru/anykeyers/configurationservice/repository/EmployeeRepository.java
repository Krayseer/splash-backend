package ru.anykeyers.configurationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.configurationservice.domain.Configuration;
import ru.anykeyers.configurationservice.domain.Employee;

import java.util.List;

/**
 * DAO для работы с работниками
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Найти всех работников автомойки
     *
     * @param configuration конфигурация автомойки
     */
    List<Employee> findByConfiguration(Configuration configuration);

    /**
     * Удалить работника
     *
     * @param configuration автомойка
     * @param userId        идентификатор пользователя
     */
    void deleteByConfigurationAndUserId(Configuration configuration, Long userId);

}
