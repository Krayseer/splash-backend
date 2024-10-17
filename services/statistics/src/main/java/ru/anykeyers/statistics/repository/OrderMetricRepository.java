package ru.anykeyers.statistics.repository;

import org.springframework.stereotype.Repository;
import ru.anykeyers.statistics.domain.OrderMetric;

@Repository
public interface OrderMetricRepository extends CarWashMetricRepository<OrderMetric> {
}
