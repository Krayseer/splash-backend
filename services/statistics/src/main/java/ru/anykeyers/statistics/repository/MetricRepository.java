package ru.anykeyers.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anykeyers.statistics.domain.CarWashMetric;
import ru.anykeyers.statistics.domain.key.CarWashMetricId;

import java.util.List;

/**
 * DAO для работы с метриками
 */
@Repository
public interface MetricRepository extends JpaRepository<CarWashMetric, CarWashMetricId> {

    /**
     * Получить список метрик автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    List<CarWashMetric> findByIdCarWashId(Long carWashId);

}
