package ru.anykeyers.statistics.repository;

import org.springframework.stereotype.Repository;
import ru.anykeyers.statistics.domain.ServiceMetric;

/**
 * DAO для работы со статистикой по услугам автомойки
 */
@Repository
public interface ServiceMetricRepository extends CarWashMetricRepository<ServiceMetric> {
}
