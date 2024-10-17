package ru.anykeyers.configurationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.configurationservice.domain.Configuration;

import java.util.Optional;
import java.util.UUID;

/**
 * DAO для работы с конфигурациями автомоек
 */
@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    /**
     * Получить конфигурацию автомойки
     *
     * @param id идентификатор пользователя
     */
    Optional<Configuration> findByUserId(UUID id);

}
