package ru.anykeyers.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import ru.anykeyers.statistics.domain.CarWashMetric;

/**
 * DAO для работы с метриками автомойки
 */
@NoRepositoryBean
public interface CarWashMetricRepository<T extends CarWashMetric> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    /**
     * Получить метрики для автомойки
     *
     * @param carWashId идентификатор автомойки
     */
    T findByCarWashId(Long carWashId);

}
