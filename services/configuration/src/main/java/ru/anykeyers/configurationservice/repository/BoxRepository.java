package ru.anykeyers.configurationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.configurationservice.domain.Box;
import ru.anykeyers.configurationservice.domain.Configuration;

import java.util.List;

/**
 * DAO для работы с боксами
 */
@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

    /**
     * Получить список боксов
     *
     * @param configuration конфигурация
     */
    List<Box> findByConfiguration(Configuration configuration);

}
